package listing6p13;

import dummy.ImageData;
import dummy.ImageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static listing5p13.Listing5P13.launderThrowable;

public class FutureRenderer {
    private static final int NTHREADS = 100;
    private final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);

    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task =
                new Callable<List<ImageData>>() {
                    public List<ImageData> call() {
                        List<ImageData> result = new ArrayList<ImageData>();
                        for (ImageInfo imageInfo : imageInfos) {
                            result.add(imageInfo.downloadImage());
                        }
                        return result;
                    }
                };
        Future<List<ImageData>> future = executor.submit(task);
        renderText(source);
        try {
            List<ImageData> imageData = future.get();
            for (ImageData data : imageData) {
                renderImage(data);
            }
        } catch (InterruptedException e) {
            // Re-assert the thread's interrupted status
            Thread.currentThread().interrupt();
            // We don't need the result, so cancel the task too
            future.cancel(true);
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }

    /*
    Dummy Implementation
     */
    private void renderImage(ImageData data) {
    }

    /*
    Dummy Implementation
     */
    private void renderText(CharSequence source) {
    }

    /*
    Dummy Implementation
     */
    private List<ImageInfo> scanForImageInfo(CharSequence source) {
        return new ArrayList<>();
    }
}
