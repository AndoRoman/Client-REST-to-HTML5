import io.javalin.Javalin;
import visual.ControladorPlantilla;

public class Main {

    public static void main(String[] args) {
        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config ->{
        }).start(7000);


        new ControladorPlantilla().rutas(app);
    }

}
