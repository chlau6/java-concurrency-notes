package listing6p15;

import dummy.ImageData;
import dummy.ImageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static listing5p13.Listing5P13.launderThrowable;

public class Renderer {
    private final ExecutorService executor;
    Renderer(ExecutorService executor) {
        this.executor = executor;
    }

    void renderPage(CharSequence source) {
        final List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<ImageData>(executor);
        for (final ImageInfo imageInfo : info) {
            completionService.submit(new Callable<ImageData>() {
                public ImageData call() {
                    return imageInfo.downloadImage();
                }
            });
        }

        renderText(source);
        try {
            for (int t = 0, n = info.size(); t < n; t++) {
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }

    /*
    Dummy Implementation
     */
    private List<ImageInfo> scanForImageInfo(CharSequence source) {
        return new ArrayList<>();
    }

    /*
    Dummy Implementation
     */
    private void renderImage(ImageData imageData) {
    }

    /*
    Dummy Implementation
     */
    private void renderText(CharSequence source) {
    }
}

/*
We can use a CompletionService to improve the performance of the page renderer in two ways:
shorter total runtime and improved responsiveness.
We can create a separate task for downloading each image and execute them in a thread pool,
turning the sequential download into a parallel one:
this reduces the amount of time to download all the images.
And by fetching results from the CompletionService and rendering each image as soon as it is available,
we can give the user a more dynamic and responsive user interface.
This implementation is shown in Renderer in Listing 6.15.
 */
