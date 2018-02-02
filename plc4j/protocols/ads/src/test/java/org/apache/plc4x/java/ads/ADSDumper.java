/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package org.apache.plc4x.java.ads;

import org.apache.plc4x.java.ads.api.commands.ADSWriteRequest;
import org.apache.plc4x.java.ads.api.commands.types.Data;
import org.apache.plc4x.java.ads.api.commands.types.IndexGroup;
import org.apache.plc4x.java.ads.api.commands.types.IndexOffset;
import org.apache.plc4x.java.ads.api.generic.AMSHeader;
import org.apache.plc4x.java.ads.api.generic.AMSTCPHeader;
import org.apache.plc4x.java.ads.api.generic.types.*;
import org.pcap4j.core.PcapDumper;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.*;
import org.pcap4j.util.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class ADSDumper {

    private static final Logger logger = LoggerFactory.getLogger(ADSDumper.class);

    public static final void main(String... args) throws Exception {
        Path dumpFile = Files.createTempFile("pcapdump", ".pcap");

        try (PcapHandle handle = Pcaps.openDead(DataLinkType.EN10MB, 65536);
             PcapDumper dumper = handle.dumpOpen(dumpFile.toAbsolutePath().toString())) {

            ADSWriteRequest adsWriteRequest = new ADSWriteRequest(
                AMSTCPHeader.of(13),
                new AMSHeader(
                    AMSNetId.of("0.0.0.0.0.0"),
                    AMSPort.of(13),
                    AMSNetId.of("0.0.0.0.0.0"),
                    AMSPort.of(13),
                    Command.ADS_Write,
                    State.ADS_REQUEST_TCP,
                    DataLength.of(13),
                    AMSError.of((byte) 0, (byte) 0, (byte) 0, (byte) 0),
                    Invoke.of((byte) 0, (byte) 0, (byte) 0, (byte) 0),
                    org.apache.plc4x.java.ads.api.generic.types.Data.of((byte) 0)
                ),
                IndexGroup.of((byte) 0, (byte) 0, (byte) 0, (byte) 0),
                IndexOffset.of((byte) 0, (byte) 0, (byte) 0, (byte) 0),
                Length.of(13),
                Data.of((byte) 0x42)
            );

            UnknownPacket.Builder amsPacket = new UnknownPacket.Builder();
            amsPacket.rawData(adsWriteRequest.getBytes());

            TcpPacket.Builder tcpPacketBuilder = new TcpPacket.Builder();
            tcpPacketBuilder
                .srcAddr(InetAddress.getLocalHost())
                .srcPort(TcpPort.getInstance((short) 13))
                .dstAddr(InetAddress.getLocalHost())
                .dstPort(TcpPort.getInstance((short) 48898))
                .payloadBuilder(amsPacket)
                .correctChecksumAtBuild(true)
                .correctLengthAtBuild(true);

            IpV4Packet.Builder ipv4PacketBuilder = new IpV4Packet.Builder();
            ipv4PacketBuilder
                .version(IpVersion.IPV4)
                .tos(IpV4Rfc1349Tos.newInstance((byte) 0x75))
                .protocol(IpNumber.TCP)
                .srcAddr((Inet4Address) InetAddress.getLocalHost())
                .dstAddr((Inet4Address) InetAddress.getLocalHost())
                .payloadBuilder(tcpPacketBuilder)
                .correctChecksumAtBuild(true)
                .correctLengthAtBuild(true);

            EthernetPacket.Builder etherPacketBuilder = new EthernetPacket.Builder();
            etherPacketBuilder
                .srcAddr(MacAddress.getByName("fe:00:00:00:00:01"))
                .dstAddr(MacAddress.getByName("fe:00:00:00:00:02"))
                .type(EtherType.IPV4)
                .payloadBuilder(ipv4PacketBuilder)
                .paddingAtBuild(true);

            dumper.dump(etherPacketBuilder.build());
            dumper.flush();

            logger.info("Wrote {}", dumpFile);
        }
    }
}