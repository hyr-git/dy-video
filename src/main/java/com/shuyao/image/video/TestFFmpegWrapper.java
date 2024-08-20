package com.shuyao.image.video;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * 使用ffmpeg将视频切割成多个ts分片
 */
public class TestFFmpegWrapper {

    public void testVideo() throws IOException {

        FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFprobe ffprobe = new FFprobe("ffprobe");

        final FFmpegProbeResult probe = ffprobe.probe("e:/hls/Spring_Insight-Milestone_3-Screencast.mp4");

        final List<FFmpegStream> streams = probe.getStreams().stream().filter(fFmpegStream -> fFmpegStream.codec_type != null).collect(Collectors.toList());

        final Optional<FFmpegStream> audioStream = streams.stream().filter(fFmpegStream -> FFmpegStream.CodecType.AUDIO.equals(fFmpegStream.codec_type)).findFirst();

        final Optional<FFmpegStream> videoStream = streams.stream().filter(fFmpegStream -> FFmpegStream.CodecType.VIDEO.equals(fFmpegStream.codec_type)).findFirst();

        if (!audioStream.isPresent()) {
            System.err.println("未发现音频流");
        }

        if (!videoStream.isPresent()) {
            System.err.println("未发现视频流");
        }


        FFmpegBuilder builder = new FFmpegBuilder()

                .setInput("e:/hls/Spring_Insight-Milestone_3-Screencast.mp4")     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput("e:/hls/output.mp4")   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                //.setTargetSize(250_000)  // Aim for a 250KB file
                .setAudioBitRate(audioStream.isPresent() ? audioStream.get().bit_rate : 0)

                .setVideoBitRate(videoStream.isPresent() ? videoStream.get().bit_rate : 0)

                .disableSubtitle()       // No subtiles

                .setAudioChannels(1)         // Mono audio
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(48_000)  // at 48KHz
                .setAudioBitRate(32768)      // at 32 kbit/s

                .setVideoCodec("libx264")     // Video using x264
                .setVideoFrameRate(24, 1)     // at 24 frames per second
                .setVideoResolution(640, 480) // at 640x480 resolution

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Run a one-pass encode
        executor.createJob(builder).run();

        // Or run a two-pass encode (which is better quality at the cost of being slower)
        executor.createTwoPassJob(builder).run();
    }


    public void testAudio() throws IOException {
        FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFprobe ffprobe = new FFprobe("ffprobe");

        final FFmpegProbeResult probe = ffprobe.probe("e:/hls/test/output.wav");

        final List<FFmpegStream> streams = probe.getStreams().stream().filter(fFmpegStream -> fFmpegStream.codec_type != null).collect(Collectors.toList());

        final Optional<FFmpegStream> audioStream = streams.stream().filter(fFmpegStream -> FFmpegStream.CodecType.AUDIO.equals(fFmpegStream.codec_type)).findFirst();

        if (!audioStream.isPresent()) {
            System.err.println("未发现音频流");
        }

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput("e:/hls/test/output.wav")
                .overrideOutputFiles(true)
                .addOutput("e:/hls/test/output-wav.m3u8")
                .setFormat("wav")
                .setAudioBitRate(audioStream.isPresent() ? audioStream.get().bit_rate : 0)
                .setAudioChannels(1)
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(audioStream.get().sample_rate)
                .setAudioBitRate(audioStream.get().bit_rate)
                .setStrict(FFmpegBuilder.Strict.STRICT)
                .setFormat("hls")
                .addExtraArgs("-hls_wrap", "0", "-hls_time", "5", "-hls_list_size", "0")
                .done();


        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Run a one-pass encode
        executor.createJob(builder).run();
    }
}