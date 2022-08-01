package listing6p10;

import dummy.ImageData;
import dummy.ImageInfo;

import java.util.ArrayList;
import java.util.List;

public class SingleThreadRenderer {
    void renderPage(CharSequence source) {
        renderText(source);
        List<ImageData> imageData = new ArrayList<ImageData>();
        for (ImageInfo imageInfo : scanForImageInfo(source))
            imageData.add(imageInfo.downloadImage());
        for (ImageData data : imageData)
            renderImage(data);
    }

    /* Dummy Implementation */
    private ImageInfo[] scanForImageInfo(CharSequence source) {
        return new ImageInfo[] {};
    }

    /* Dummy Implementation */
    private void renderImage(ImageData data) {
    }

    /* Dummy Implementation */
    private void renderText(CharSequence source) {
    }
}

/*
In this section we develop several versions of a component that admit varying degrees of concurrency.
Our sample component is the page-rendering portion of a browser application,
which takes a page of HTML and renders it into an image buffer.
To keep it simple, we assume that the HTML consists only of marked up text interspersed with image elements with
pre-specified dimensions and URLs.

The simplest approach is to process the HTML document sequentially. As text markup is encountered,
render it into the image buffer; as image references are encountered,
fetch the image over the network and draw it into the image buffer as well.
This is easy to implement and requires touching each element of the input only once
(it doesn't even require buffering the document),
but is likely to annoy the user, who may have to wait a long time before all the text is rendered.

A less annoying but still sequential approach involves rendering the text elements first,
leaving rectangular placeholders for the images, and after completing the initial pass on the document,
going back and downloading the images and drawing them into the associated placeholder.
This approach is shown in SingleThreadRenderer in Listing 6.10.

Downloading an image mostly involves waiting for I/O to complete, and during this time the CPU does little work.
So the sequential approach may underutilize the CPU,
and also makes the user wait longer than necessary to see the finished page.
We can achieve better utilization and responsiveness by
breaking the problem into independent tasks that can execute concurrently
 */
