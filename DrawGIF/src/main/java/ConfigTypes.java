import org.bukkit.map.MapView;

public class ConfigTypes {

    public static class MapPiece {
        MapView mapView;
        String filename;
        int x;
        int y;

        public MapPiece(MapView mapView, String filename, int x, int y){
            this.mapView = mapView;
            this.filename = filename;
            this.x = x;
            this.y = y;
        }
    }

}
