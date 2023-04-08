package ffmpeg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BufferedImageWriter {
    private final String FFMPEG_PATH;
    private final int width;
    private final int height;
    private final int framerate;
    private final String videoCodec;
    private final String pixelFormat;

    private Process proc = null;

    public BufferedImageWriter(String ffmpegPath,
                               int width,
                               int height,
                               int framerate,
                               String videoCodec,
                               String pixelFormat) {
        FFMPEG_PATH = ffmpegPath;
        this.width = width;
        this.height = height;
        this.framerate = framerate;
        this.videoCodec = videoCodec;
        this.pixelFormat = pixelFormat;
    }
    public BufferedImageWriter(String ffmpegPath,
                               int width,
                               int height,
                               int framerate) {
        FFMPEG_PATH = ffmpegPath;
        this.width = width;
        this.height = height;
        this.framerate = framerate;
        this.videoCodec = null;
//        this.videoCodec = "libx264";
        this.pixelFormat = "yuv420p";
//        this.pixelFormat = "rgb24";
    }

    public void initializeWriting() throws IOException {
//        if self.pixel_format.startswith("yuv"):
//        vf_arg += f',eq=saturation={self.saturation}:gamma={self.gamma}'

        List<String> args = new ArrayList<>();
        args.add(FFMPEG_PATH);
        args.add("-y"); // overwrite output file if it exists
        args.add("-f");
        args.add("image2pipe");
        args.add("-s");
        args.add(width + "x" + height); // size of one frame
//        args.add("-pix_fmt");
//        args.add("rgba");
        args.add("-r");
        args.add(String.valueOf(framerate)); // fps
        args.add("-i");
        args.add("-"); // input comes from a pipe
//        args.add("-vf");
//        args.add("vflip");
        args.add("-an"); // don't expect any audio
//        args.add("-loglevel");
//        args.add("error");

        if (videoCodec != null) {
            args.add("-vcodec");
            args.add(videoCodec);
        }
        if (pixelFormat != null) {
            args.add("-pix_fmt");
            args.add(pixelFormat);
        }

        args.add("output.mp4");

        proc = new ProcessBuilder(args)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();

//        proc.getInputStream().
    }

    public void writeImage(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        baos.writeTo(proc.getOutputStream());
    }

    public void completeWriting() throws IOException {
        System.out.println("ending writing");
        proc.getOutputStream().close();

        try {
            proc.waitFor();
            System.out.println("waited, killing");
//            proc.destroy();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
