package Logic;

public class Ubicacion {
    private String longitud;
    private String latitud;

    public Ubicacion(String longitud, String latitud) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
}

