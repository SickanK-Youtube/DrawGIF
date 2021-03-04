
public class GifMapHandler {
    private final GifMapRenderer[] gifMapRenderers;
    private final int delay;

    public GifMapHandler(GifMapRenderer[] gifMapRenderers, int delay) {
        this.gifMapRenderers = gifMapRenderers;
        this.delay = delay;

        render();
    }

    private void render() {
        while (true) {
            for (GifMapRenderer gifMapRenderer : gifMapRenderers) {
                gifMapRenderer.next();
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
