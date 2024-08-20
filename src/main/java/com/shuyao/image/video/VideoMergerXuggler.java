package com.shuyao.image.video;


import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.*;
import java.io.File;

public class VideoMergerXuggler {
    String outputPath = "D:\\showFile\\viedu\\output.mp4";

        private static final String OUTPUT_FILE = "D:\\showFile\\viedu\\merged_video.mp4";

        public static void main(String[] args) {
            String videoPath1 = "D:\\showFile\\viedu\\001.mp4";
            String videoPath2 = "D:\\showFile\\viedu\\002.mp4";
            IMediaWriter writer = ToolFactory.makeWriter(OUTPUT_FILE);

            String[] inputFiles = {videoPath1, videoPath2};

            for (String inputFile : inputFiles) {
                IContainer container = IContainer.make();
                if (container.open(inputFile, IContainer.Type.READ, null) < 0) {
                    throw new IllegalArgumentException("Could not open video file: " + inputFile);
                }

                int numStreams = container.getNumStreams();
                int videoStreamId = -1;
                IStreamCoder videoCoder = null;

                // 寻找视频流
                for (int i = 0; i < numStreams; i++) {
                    IStream stream = container.getStream(i);
                    IStreamCoder coder = stream.getStreamCoder();

                    if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                        videoStreamId = i;
                        videoCoder = coder;
                        break;
                    }
                }

                if (videoStreamId == -1) {
                    throw new RuntimeException("Could not find video stream in container: " + inputFile);
                }

                if (videoCoder.open() < 0) {
                    throw new RuntimeException("Could not open video coder for container: " + inputFile);
                }

                writer.addVideoStream(0, 0, videoCoder.getWidth(), videoCoder.getHeight());

                IPacket packet = IPacket.make();

                while (container.readNextPacket(packet) >= 0) {
                    if (packet.getStreamIndex() == videoStreamId) {
                        IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
                                videoCoder.getWidth(), videoCoder.getHeight());
                        int offset = 0;
                        while (offset < packet.getSize()) {
                            int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
                            if (bytesDecoded < 0) {
                                throw new RuntimeException("Error decoding video: " + inputFile);
                            }
                            offset += bytesDecoded;

                            if (picture.isComplete()) {
                                writer.encodeVideo(0, picture);
                            }
                        }
                    }
                }

                videoCoder.close();
                container.close();
            }

            writer.close();
        }
}
