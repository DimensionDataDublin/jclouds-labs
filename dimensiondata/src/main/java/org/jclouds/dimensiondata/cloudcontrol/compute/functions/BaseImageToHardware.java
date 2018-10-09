/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.dimensiondata.cloudcontrol.compute.functions;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.HardwareBuilder;
import org.jclouds.compute.domain.Processor;
import org.jclouds.compute.domain.Volume;
import org.jclouds.compute.domain.VolumeBuilder;
import org.jclouds.dimensiondata.cloudcontrol.domain.BaseImage;
import org.jclouds.dimensiondata.cloudcontrol.domain.CPU;
import org.jclouds.dimensiondata.cloudcontrol.domain.CpuSpeed;
import org.jclouds.dimensiondata.cloudcontrol.domain.Floppy;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeController;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDevice;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDeviceOrDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.IdeDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataController;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDevice;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDeviceOrDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.SataDisk;
import org.jclouds.dimensiondata.cloudcontrol.domain.ScsiController;
import org.jclouds.dimensiondata.cloudcontrol.domain.ScsiDisk;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BaseImageToHardware implements Function<BaseImage, Hardware> {

   private static final int GB_TO_MB_MULTIPLIER = 1024;

   @Override
   public Hardware apply(final BaseImage from) {
      HardwareBuilder builder = new HardwareBuilder().ids(from.id()).name(from.name()).hypervisor("vmx")
            .processors(buildProcessorList(from.cpu())).ram(from.memoryGb() * GB_TO_MB_MULTIPLIER);

      FluentIterable<Volume> completeVolumeSet = processIdeControllers(from,
            FluentIterable.from(new ArrayList<Volume>()));
      completeVolumeSet = processSataControllers(from, completeVolumeSet);
      completeVolumeSet = processScsiControllers(from, completeVolumeSet);
      completeVolumeSet = processFloppies(from, completeVolumeSet);
      builder.volumes(completeVolumeSet);
      return builder.build();
   }

   private List<Processor> buildProcessorList(final CPU cpu) {
      final List<Processor> processorList = new ArrayList<Processor>();
      final double speed = CpuSpeed.fromDimensionDataSpeed(cpu.speed()).getSpeed();
      for (int count = 0; count < cpu.count(); count++) {
         processorList.add(new Processor(cpu.coresPerSocket(), speed));
      }
      return processorList;
   }

   private FluentIterable<Volume> processIdeControllers(final BaseImage from,
         FluentIterable<Volume> completeVolumeSet) {
      if (from.ideControllers() != null) {
         completeVolumeSet = completeVolumeSet.append(
               FluentIterable.from(buildIdeDiskList(from.ideControllers())).transform(new Function<IdeDisk, Volume>() {
                  @Override
                  public Volume apply(final IdeDisk disk) {
                     return new VolumeBuilder().id(disk.id()).device(disk.slot()).size((float) disk.sizeGb())
                           .type(Volume.Type.LOCAL).build();
                  }
               }).toSet());

         completeVolumeSet = completeVolumeSet.append(FluentIterable.from(buildIdeDeviceList(from.ideControllers()))
               .transform(new Function<IdeDevice, Volume>() {
                  @Override
                  public Volume apply(final IdeDevice device) {
                     return new VolumeBuilder().id(device.id()).device(device.slot()).size((float) device.sizeGb())
                           .type(Volume.Type.LOCAL).build();
                  }
               }).toSet());
      }
      return completeVolumeSet;
   }

   private FluentIterable<Volume> processSataControllers(final BaseImage from,
         FluentIterable<Volume> completeVolumeSet) {
      if (from.sataControllers() != null) {
         completeVolumeSet = completeVolumeSet.append(FluentIterable.from(buildSataDiskList(from.sataControllers()))
               .transform(new Function<SataDisk, Volume>() {
                  @Override
                  public Volume apply(final SataDisk disk) {
                     return new VolumeBuilder().id(disk.id()).device(disk.sataId()).size((float) disk.sizeGb())
                           .type(Volume.Type.LOCAL).build();
                  }
               }).toSet());

         completeVolumeSet = completeVolumeSet.append(FluentIterable.from(buildSataDeviceList(from.sataControllers()))
               .transform(new Function<SataDevice, Volume>() {
                  @Override
                  public Volume apply(final SataDevice device) {
                     return new VolumeBuilder().id(device.id()).device(device.sataId()).size((float) device.sizeGb())
                           .type(Volume.Type.LOCAL).build();
                  }
               }).toSet());
      }
      return completeVolumeSet;
   }

   private FluentIterable<Volume> processScsiControllers(final BaseImage from,
         FluentIterable<Volume> completeVolumeSet) {
      if (from.scsiControllers() != null) {
         completeVolumeSet = completeVolumeSet.append(FluentIterable.from(buildScsiDiskList(from.scsiControllers()))
               .transform(new Function<ScsiDisk, Volume>() {
                  @Override
                  public Volume apply(final ScsiDisk disk) {
                     return new VolumeBuilder().id(disk.id()).device(disk.scsiId()).size((float) disk.sizeGb())
                           .type(Volume.Type.LOCAL).build();
                  }
               }).toSet());
      }
      return completeVolumeSet;
   }

   private FluentIterable<Volume> processFloppies(final BaseImage from, FluentIterable<Volume> completeVolumeSet) {
      if (from.floppies() != null) {
         completeVolumeSet = completeVolumeSet.append(
               FluentIterable.from(buildFloppiesList(from.floppies())).transform(new Function<Floppy, Volume>() {
                  @Override
                  public Volume apply(final Floppy floppy) {
                     return new VolumeBuilder().id(floppy.id()).device(floppy.key() + ":" + floppy.driveNumber())
                           .size((float) floppy.sizeGb()).type(Volume.Type.LOCAL).build();
                  }
               }).toSet());
      }
      return completeVolumeSet;
   }

   private List<IdeDisk> buildIdeDiskList(final List<IdeController> controllers) {
      final List<IdeDisk> diskList = new ArrayList<>();
      for (int count = 0; count < controllers.size(); count++) {
         for (IdeDeviceOrDisk deviceOrDisk : controllers.get(count).deviceOrDisks()) {
            if (deviceOrDisk.disk() != null) {
               IdeDisk ideDisk = deviceOrDisk.disk();
               diskList.add(IdeDisk.create(ideDisk.id(),
                     constructUniqueVolumeIdentifier(controllers.get(count).key(), ideDisk.slot()), ideDisk.sizeGb(),
                     ideDisk.speed(), ideDisk.state()));
            }
         }
      }
      return diskList;
   }

   private List<IdeDevice> buildIdeDeviceList(final List<IdeController> controllers) {
      final List<IdeDevice> deviceList = new ArrayList<>();
      for (int count = 0; count < controllers.size(); count++) {
         for (IdeDeviceOrDisk deviceOrDisk : controllers.get(count).deviceOrDisks()) {
            if (deviceOrDisk.device() != null) {
               IdeDevice ideDevice = deviceOrDisk.device();
               if (ideDevice.sizeGb() > 0) {
                  deviceList.add(IdeDevice.create(ideDevice.id(),
                        constructUniqueVolumeIdentifier(controllers.get(count).key(), ideDevice.slot()),
                        ideDevice.type(), ideDevice.sizeGb(), ideDevice.fileName(), ideDevice.state()));
               }
            }
         }
      }
      return deviceList;
   }

   private List<SataDisk> buildSataDiskList(final List<SataController> controllers) {
      final List<SataDisk> diskList = new ArrayList<>();
      for (int count = 0; count < controllers.size(); count++) {
         for (SataDeviceOrDisk deviceOrDisk : controllers.get(count).deviceOrDisks()) {
            if (deviceOrDisk.disk() != null) {
               SataDisk sataDisk = deviceOrDisk.disk();
               diskList.add(SataDisk.create(sataDisk.id(),
                     constructUniqueVolumeIdentifier(controllers.get(count).key(), sataDisk.sataId()),
                     sataDisk.sizeGb(), sataDisk.speed(), sataDisk.state()));
            }
         }
      }
      return diskList;
   }

   private List<SataDevice> buildSataDeviceList(final List<SataController> controllers) {
      final List<SataDevice> deviceList = new ArrayList<>();
      for (int count = 0; count < controllers.size(); count++) {
         for (SataDeviceOrDisk deviceOrDisk : controllers.get(count).deviceOrDisks()) {
            if (deviceOrDisk.device() != null) {
               SataDevice sataDevice = deviceOrDisk.device();
               if (sataDevice.sizeGb() > 0) {
                  deviceList.add(SataDevice.create(sataDevice.id(),
                        constructUniqueVolumeIdentifier(controllers.get(count).key(), sataDevice.sataId()),
                        sataDevice.type(), sataDevice.sizeGb(), sataDevice.fileName(), sataDevice.state()));
               }
            }
         }
      }
      return deviceList;
   }

   private List<ScsiDisk> buildScsiDiskList(final List<ScsiController> controllers) {
      final List<ScsiDisk> diskList = new ArrayList<>();
      for (int count = 0; count < controllers.size(); count++) {
         for (ScsiDisk scsiDisk : controllers.get(count).disks()) {
            diskList.add(ScsiDisk.create(scsiDisk.id(),
                  constructUniqueVolumeIdentifier(controllers.get(count).key(), scsiDisk.scsiId()), scsiDisk.sizeGb(),
                  scsiDisk.speed(), scsiDisk.state()));
         }
      }
      return diskList;
   }

   private List<Floppy> buildFloppiesList(final List<Floppy> floppies) {
      List<Floppy> resultList = new ArrayList<>();
      for (Floppy floppy : floppies) {
         if (floppy.sizeGb() > 0) {
            resultList.add(floppy);
         }
      }
      return resultList;
   }

   private String constructUniqueVolumeIdentifier(int controllerKey, String diskOrDeviceId) {
      return controllerKey + ":" + diskOrDeviceId;
   }

}
