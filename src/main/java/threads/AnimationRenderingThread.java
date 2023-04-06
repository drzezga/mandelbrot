package threads;

import io.humble.video.*;
//import org.jcodec.api.awt.AWTSequenceEncoder;
//import org.jcodec.common.io.NIOUtils;
//import org.jcodec.common.model.*;
//import org.jcodec.common.model.Rational;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;
import ui.tabs.AnimationTab;
import ui.animation.Keyframe;
import misc.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class AnimationRenderingThread extends RenderingManagerThread {

    private RenderData[] frameData;
    private AnimationTab at;
    private int frameIndex = 0;

//    private BufferedImage[] frames;
//    private BufferedImage frame;
    private BufferedImage lastFrame;

    Encoder encoder;
    MediaPacket packet;
    MediaPictureConverter converter;
    MediaPicture picture;
    Muxer muxer;
    int imageIndex = 0;

    public AnimationRenderingThread(ThreadType threadType, AnimationTab at) {
        super(new BufferedImage(SettingsManager.getResolutionX(), SettingsManager.getResolutionY(), BufferedImage.TYPE_3BYTE_BGR), threadType, 0, 0, SettingsManager.getResolutionX(), SettingsManager.getResolutionY());

        this.frameData = generateFrameData(at);
        this.at = at;
    }

    @Override
    public void run() {
        threadRun();
        startTime = System.nanoTime();
        at.setTime(0);
        at.setMaxIterations(Math.abs(frameData.length));
//        frames = new BufferedImage[frameData.length];

        beginVideo();

        startThreads();

        while (areThreadsRunning()) {
            try {
                at.setProgress(frameIndex);
                sleep(25);
            } catch (InterruptedException ignored) {}
        }

//        imagesToVideo();

        endVideo();

        at.setTime((float)(System.nanoTime() - startTime) / 1000000000);
        System.out.println("Animation rendering finished");
        try {
            Desktop.getDesktop().open(new File("output.mp4"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextFrame() {
        System.out.println("Next frame " + frameIndex);
//        lastFrame = frame;
        if (frameIndex == frameData.length) return;
        BufferedImage frame = postProcess(image);
//        frames[frameIndex] = image;
//        frame = image;
        saveImageToVideo(frame);

        frameIndex++;

        lastFrame = frame;
        frame = createImage(new BufferedImage(SettingsManager.getResolutionX(), SettingsManager.getResolutionY(), BufferedImage.TYPE_3BYTE_BGR));
        pixelsLeft = w * h;
    }

    // Example from https://github.com/artclarke/humble-video/blob/master/humble-video-demos/src/main/java/io/humble/video/demos/RecordAndEncodeVideo.java

    private void beginVideo() {
        final Rational framerate = Rational.make(1, at.getFramerate());

        /** First we create a muxer using the passed in filename and formatname if given. */
        muxer = Muxer.make("output.mp4", null, "mp4");

        /** Now, we need to decide what type of codec to use to encode video. Muxers
         * have limited sets of codecs they can use. We're going to pick the first one that
         * works, or if the user supplied a codec name, we're going to force-fit that
         * in instead.
         */
        final MuxerFormat format = muxer.getFormat();
        final Codec codec;
        codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());

        /**
         * Now that we know what codec, we need to create an encoder
         */
        encoder = Encoder.make(codec);

        /**
         * Video encoders need to know at a minimum:
         *   width
         *   height
         *   pixel format
         * Some also need to know frame-rate (older codecs that had a fixed rate at which video files could
         * be written needed this). There are many other options you can set on an encoder, but we're
         * going to keep it simpler here.
         */
        encoder.setWidth(w);
        encoder.setHeight(h);
        // We are going to use 420P as the format because that's what most video formats these days use
        final PixelFormat.Type pixelFormat = PixelFormat.Type.PIX_FMT_YUV420P;
        encoder.setPixelFormat(pixelFormat);
        encoder.setTimeBase(framerate);

        /** An annoynace of some formats is that they need global (rather than per-stream) headers,
         * and in that case you have to tell the encoder. And since Encoders are decoupled from
         * Muxers, there is no easy way to know this beyond
         */
        if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER))
            encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);

        /** Open the encoder. */
        encoder.open(null, null);


        /** Add this stream to the muxer. */
        muxer.addNewStream(encoder);

        /** And open the muxer for business. */
        try {
            muxer.open(null, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        BufferedImage.TYPE_3BYTE_BGR;
//        BufferedImage.TYPE_INT_ARGB;
        /** Next, we need to make sure we have the right MediaPicture format objects
         * to encode data with. Java (and most on-screen graphics programs) use some
         * variant of Red-Green-Blue image encoding (a.k.a. RGB or BGR). Most video
         * codecs use some variant of YCrCb formatting. So we're going to have to
         * convert. To do that, we'll introduce a MediaPictureConverter object later. object.
         */
        converter = null;
        picture = MediaPicture.make(
                encoder.getWidth(),
                encoder.getHeight(),
                pixelFormat);
        picture.setTimeBase(framerate);


        /** Now begin our main loop of taking screen snaps.
         * We're going to encode and then write out any resulting packets. */

        at.setMaxIterations(frameData.length);

        packet = MediaPacket.make();
    }

    public void saveImageToVideo(BufferedImage screen) {
        /** Make the screen capture && convert image to TYPE_3BYTE_BGR */
//         = frame;
        if (screen == null) {
            System.err.println("Frame is empty " + imageIndex);
            screen = lastFrame;
        }

        /** This is LIKELY not in YUV420P format, so we're going to convert it using some handy utilities. */
        if (converter == null)
            converter = MediaPictureConverterFactory.createConverter(screen, picture);
        converter.toPicture(picture, screen, imageIndex);

        do {
            encoder.encode(packet, picture);
            if (packet.isComplete())
                muxer.write(packet, false);
        } while (packet.isComplete());
//        at.setProgress(imageIndex);
        imageIndex++;
    }

    private void endVideo() {
        /** Encoders, like decoders, sometimes cache pictures so it can do the right key-frame optimizations.
         * So, they need to be flushed as well. As with the decoders, the convention is to pass in a null
         * input until the output is not complete.
         */
        do {
            encoder.encode(packet, null);
            if (packet.isComplete())
                muxer.write(packet,  false);
        } while (packet.isComplete());

        /** Finally, let's clean up after ourselves. */
        muxer.close();
    }

    private void imagesToVideo() {
//        AWTSequenceEncoder enc;
//        try {
//            enc = new AWTSequenceEncoder(NIOUtils.writableFileChannel("output.mp4"), Rational.R(at.getFramerate(), 1));
////            enc = AWTSequenceEncoder.createSequenceEncoder(new File("output.mp4"), 60);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        at.setMaxIterations(frameData.length);
//        for (int i = 0; i < frameData.length; i++) {
//            try {
//                if (frames[i] == null) {
//                    System.err.println("Frame is empty " + i);
//                    frames[i] = frames[i - 1];
////                    return;
//                }
//                try {
//                    enc.encodeImage(frames[i]);
//                } catch (BufferOverflowException e) {
//                    System.err.println(i + " has caused a BufferOverflowException. Restarting");
//                    try {
//                        enc.finish();
//                    } catch (IOException ee) {
//                        ee.printStackTrace();
//                    }
//                    enc.finish();
//                    imagesToVideo();
//                    return;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            at.setProgress(i);
//        }
//        try {
//            enc.finish();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        beginVideo();

        for (int i = 0; i < frameData.length; i++) {
            saveImageToVideo(image);
//            /** Make the screen capture && convert image to TYPE_3BYTE_BGR */
//            BufferedImage screen = frames[i];
//            if (screen == null) {
//                System.err.println("Frame is empty " + i);
//                screen = frames[i - 1];
//            }
//
//            /** This is LIKELY not in YUV420P format, so we're going to convert it using some handy utilities. */
//            if (converter == null)
//                converter = MediaPictureConverterFactory.createConverter(screen, picture);
//            converter.toPicture(picture, screen, i);
//
//            do {
//                encoder.encode(packet, picture);
//                if (packet.isComplete())
//                    muxer.write(packet, false);
//            } while (packet.isComplete());
//            at.setProgress(i);
        }

        endVideo();
    }

    public PixelRenderData fetchData() {
        if (frameIndex >= frameData.length) {
            return null;
        } else if (pixelsLeft > 0) {
            pixelsLeft--;
            int x = pixelsLeft % w + x1;
            int y = (int) Math.floor((float) pixelsLeft / w) + y1;
            if (frameData[frameIndex] == null) {
                System.out.println("Frame index null:" + frameIndex);
                return null;
            }
            return new PixelRenderData(x, y, frameData[frameIndex].center, frameData[frameIndex].zoom, screenRatio, w, h, frameData[frameIndex].threshold, colorAlgorithm, pixelsLeft == 0);
        } else {
            return new ThreadStallData(0, 0, new Complex(), 0f, 0f, 0, 0, 0, SettingsManager.getColorAlgorithm(), false);
        }
    }

    public void setPixel(int x, int y, Color color) {
        if (color == null) {
            System.err.println("Color null: " + x + " " + y);
        } else {
            try {
                image.setRGB(x, y, color.getRGB());
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.err.println("Coordinates: " + x + " | " + y);
            }
        }
    }

    private RenderData[] generateFrameData(AnimationTab at) {
        int framerate = at.getFramerate();
        RenderData[] frameData = new RenderData[(int) (at.getTimeline().getLength() * framerate)];
        ArrayList<Keyframe> kfs = at.getTimeline().getKeyframes();

        for (int i = 0; i < kfs.size(); i++) {
            Keyframe kf = kfs.get(i);
            RenderData rd = kf.getRenderData();

            if (i + 1 == kfs.size()) {
                frameData[Math.round(kf.getPosition() * framerate) - 1] = rd;
                continue;
            }

            Keyframe to = kfs.get(i + 1);
            int maxSize = (int) (to.getPosition() - kf.getPosition()) * framerate;

            switch (kf.getInterpolationType()) {
                case JUMP:
                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)] = rd;
                    }
                    break;
                case EASE_IN_OUT:
                    Complex[] _deltas = new Complex[maxSize];
                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.serp(rd, to.getRenderData(), j, maxSize);
                        if (j == 0) {
                            _deltas[0] =
                                    frameData[Math.round(kf.getPosition() * framerate + j)].center.subtract(
                                            kf.getRenderData().center);
                        } else {
                            _deltas[j] =
                                    frameData[Math.round(kf.getPosition() * framerate + j)].center.subtract(
                                            frameData[Math.round(kf.getPosition() * framerate + j - 1)].center);
                        }
                    }

                    // Relatively correct scale
                    double prevZoom = kf.getRenderData().zoom;
                    double scaleFactor = Math.pow(Math.E, Math.log(to.getRenderData().zoom / kf.getRenderData().zoom) / maxSize);
                    frameData[Math.round(kf.getPosition() * framerate)].zoom = prevZoom;

                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)].zoom = prevZoom * scaleFactor;
                        prevZoom = frameData[Math.round(kf.getPosition() * framerate + j)].zoom;
                    }

                    // Correct relative speed
                    BigDecimal _deltaPosR = to.getRenderData().center.r.subtract(kf.getRenderData().center.r);
                    BigDecimal _deltaPosI = to.getRenderData().center.i.subtract(kf.getRenderData().center.i);

                    BigDecimal _changedSumR = new BigDecimal("0");
                    BigDecimal _changedSumI = new BigDecimal("0");

                    for (int j = 0; j < maxSize; j++) {
                        _deltas[j].r = _deltas[j].r.divide(
                                new BigDecimal(Double.toString(frameData[Math.round(kf.getPosition() * framerate + j)].zoom)),
                                RoundingMode.HALF_EVEN);
                        _deltas[j].i = _deltas[j].i.divide(
                                new BigDecimal(Double.toString(frameData[Math.round(kf.getPosition() * framerate + j)].zoom)),
                                RoundingMode.HALF_EVEN);
                        _changedSumR = _changedSumR.add(_deltas[j].r);
                        _changedSumI = _changedSumI.add(_deltas[j].i);
                    }

                    BigDecimal _rMoveFactor = _deltaPosR.divide(_changedSumR, RoundingMode.HALF_EVEN);
                    BigDecimal _iMoveFactor = _deltaPosI.divide(_changedSumI, RoundingMode.HALF_EVEN);

                    for (int j = 0; j < maxSize; j++) {
                        if (j == 0) {
                            frameData[Math.round(kf.getPosition() * framerate + j)].center.r =
                                    kf.getRenderData().center.r
                                            .add(_deltas[j].r.multiply(_rMoveFactor));
                            frameData[Math.round(kf.getPosition() * framerate + j)].center.i =
                                    kf.getRenderData().center.i
                                            .add(_deltas[j].i.multiply(_iMoveFactor));
                        } else {
                            frameData[Math.round(kf.getPosition() * framerate + j)].center.r =
                                    frameData[Math.round(kf.getPosition() * framerate + j - 1)].center.r
                                            .add(_deltas[j].r.multiply(_rMoveFactor));
                            frameData[Math.round(kf.getPosition() * framerate + j)].center.i =
                                    frameData[Math.round(kf.getPosition() * framerate + j - 1)].center.i
                                            .add(_deltas[j].i.multiply(_iMoveFactor));
                        }
                    }

                    break;
                case LINEAR:
                    Complex[] deltas = new Complex[maxSize];
                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.lerp(rd, to.getRenderData(), j, maxSize);
                        if (j == 0) {
                            deltas[0] =
                                    frameData[Math.round(kf.getPosition() * framerate + j)].center.subtract(
                                    kf.getRenderData().center);
                        } else {
                            deltas[j] =
                                    frameData[Math.round(kf.getPosition() * framerate + j)].center.subtract(
                                    frameData[Math.round(kf.getPosition() * framerate + j - 1)].center);
                        }
                    }

                    // Relatively correct scale
                    double previousZoom = kf.getRenderData().zoom;
                    double scalingFactor = Math.pow(Math.E, Math.log(to.getRenderData().zoom / kf.getRenderData().zoom) / maxSize);

                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)].zoom = previousZoom * scalingFactor;
                        previousZoom = frameData[Math.round(kf.getPosition() * framerate + j)].zoom;
                    }

                    // Relatively correct speed
                    BigDecimal deltaPosR = to.getRenderData().center.r.subtract(kf.getRenderData().center.r);
                    BigDecimal deltaPosI = to.getRenderData().center.i.subtract(kf.getRenderData().center.i);

                    BigDecimal changedSumR = new BigDecimal("0");
                    BigDecimal changedSumI = new BigDecimal("0");

                    for (int j = 0; j < maxSize; j++) {
//                        frameData[Math.round(kf.getPosition() * framerate + j)].
                        deltas[j].r = deltas[j].r.divide(
                                new BigDecimal(Double.toString(frameData[Math.round(kf.getPosition() * framerate + j)].zoom)),
                                RoundingMode.HALF_EVEN);
                        deltas[j].i = deltas[j].i.divide(
                                new BigDecimal(Double.toString(frameData[Math.round(kf.getPosition() * framerate + j)].zoom)),
                                RoundingMode.HALF_EVEN);
                        changedSumR = changedSumR.add(deltas[j].r);
                        changedSumI = changedSumI.add(deltas[j].i);
                    }

                    BigDecimal rMoveFactor = deltaPosR.compareTo(BigDecimal.ZERO) == 0
                            ? BigDecimal.ONE
                            : deltaPosR.divide(changedSumR, RoundingMode.HALF_EVEN);
                    BigDecimal iMoveFactor = deltaPosI.compareTo(BigDecimal.ZERO) == 0
                            ? BigDecimal.ONE
                            : deltaPosI.divide(changedSumI, RoundingMode.HALF_EVEN);

                    for (int j = 0; j < maxSize; j++) {
                        if (j == 0) {
                            frameData[Math.round(kf.getPosition() * framerate + j)].center.r =
                                    kf.getRenderData().center.r
                                            .add(deltas[j].r.multiply(rMoveFactor));
                            frameData[Math.round(kf.getPosition() * framerate + j)].center.i =
                                    kf.getRenderData().center.i
                                            .add(deltas[j].i.multiply(iMoveFactor));
                        } else {
                            frameData[Math.round(kf.getPosition() * framerate + j)].center.r =
                                    frameData[Math.round(kf.getPosition() * framerate + j - 1)].center.r
                                            .add(deltas[j].r.multiply(rMoveFactor));
                            frameData[Math.round(kf.getPosition() * framerate + j)].center.i =
                                    frameData[Math.round(kf.getPosition() * framerate + j - 1)].center.i
                                            .add(deltas[j].i.multiply(iMoveFactor));
                        }
                    }

                    break;
                case CIRCLE_SPHERE:
                    for (int j = 0; j < maxSize; j++) {
                        frameData[Math.round(kf.getPosition() * framerate + j)] = RenderData.circleSphere(rd, to.getRenderData(), j, maxSize);
                    }
                    break;
                default:
                    System.err.println("Interpolation data is null or wrong");
            }
        }
        return frameData;
    }

}