import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private final double lonDPP0;
    private final double ROOT_WIDTH;
    private final double ROOT_HEIGHT;

    private static class BoundingBox {
        private double ullon;
        private double ullat;
        private double lrlon;
        private double lrlat;

        public BoundingBox(double ullon, double ullat, double lrlon, double lrlat) {
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlon = lrlon;
            this.lrlat = lrlat;
        }
    }

    private static class Tile {
        private final int depth;
        private final int x;
        private final int y;

        public Tile(int depth, int x, int y) {
            this.depth = depth;
            this.x = x;
            this.y = y;
        }
    }

    public Rasterer() {
        // YOUR CODE HERE
        lonDPP0 = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        ROOT_WIDTH = MapServer.ROOT_LRLON - MapServer.ROOT_ULLON;
        ROOT_HEIGHT = MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        double ullon = params.get("ullon");
        double ullat  = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat  = params.get("lrlat");
        if (ullon >= MapServer.ROOT_LRLON
            || ullat <= MapServer.ROOT_LRLAT
            || lrlon <= MapServer.ROOT_ULLON
            || lrlat >= MapServer.ROOT_ULLAT) {
            results.put("query_success", false);
            return results;
        }
        double w = params.get("w");
        double lonDPP = (lrlon - ullon) / w;
        int depth = getDepth(lonDPP);

        Tile ulTile = getTile(depth, ullon, ullat);
        Tile lrTile = getTile(depth, lrlon, lrlat);
        BoundingBox ulBox = getBoundingbox(ulTile);
        BoundingBox lrBox = getBoundingbox(lrTile);

        String[][] renderGrid = getRenderGrid(depth, ulTile.x, ulTile.y, lrTile.x, lrTile.y);

        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", ulBox.ullon);
        results.put("raster_ul_lat", ulBox.ullat);
        results.put("raster_lr_lon", lrBox.lrlon);
        results.put("raster_lr_lat", lrBox.lrlat);
        results.put("depth", depth);
        results.put("query_success", true);

        return results;
    }

    private int getDepth(double lonDPP) {
        double t = lonDPP0;
        int depth = 0;
        while (t > lonDPP && depth < 7) {
            t /= 2;
            depth++;
        }
        return depth;
    }

    private BoundingBox getBoundingbox(int depth, int x, int y) {
        double unitWidth = ROOT_WIDTH;
        double unitHeight = ROOT_HEIGHT;
        while (depth > 0) {
            unitWidth /= 2;
            unitHeight /= 2;
            depth--;
        }

        double ullon = MapServer.ROOT_ULLON + x * unitWidth;
        double ullat = MapServer.ROOT_ULLAT - y * unitHeight;
        double lrlon = ullon + unitWidth;
        double lrlat = ullat - unitHeight;

        return new BoundingBox(ullon, ullat, lrlon, lrlat);
    }

    private BoundingBox getBoundingbox(Tile tile) {
        return getBoundingbox(tile.depth, tile.x, tile.y);
    }

    private boolean inBoundingBox(double lon, double lat, BoundingBox box) {
        return box.ullon <= lon && box.ullat >= lat && box.lrlon >= lon && box.lrlat <= lat;
    }

    private Tile getTile(int depth, double lon, double lat) {
        int N = 1 << depth;     // 2 to the depth
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                BoundingBox box = getBoundingbox(depth, i, j);
                if (inBoundingBox(lon, lat, box)) {
                    return new Tile(depth, i, j);
                }
            }
        }
        throw new IllegalArgumentException("coordinate out of bounds");
    }

    private String[][] getRenderGrid(int depth, int ulX, int ulY, int lrX, int lrY) {
        int xRange = lrX - ulX + 1;
        int yRange = lrY - ulY + 1;
        String[][] renderGrid = new String[yRange][xRange];
        for (int i = 0; i < yRange; i++) {
            for (int j = 0; j < xRange; j++) {
                renderGrid[i][j] = "d" + depth + "_x" + (ulX + j) + "_y" + (ulY + i) + ".png";
            }
        }
        return renderGrid;
    }


}
