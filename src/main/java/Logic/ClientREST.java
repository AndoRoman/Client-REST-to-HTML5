package Logic;

import com.google.gson.JsonObject;
import kong.unirest.GenericType;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import kong.unirest.HttpResponse;

import java.util.List;

public class ClientREST {

    private static int idGlobal;

    private static String URLApi = "http://localhost:8043/api-Reset/formulario";

    public static boolean consultarUsuario(String usuario){

        String repuesta = Unirest.get("http://localhost:8043/api-Reset/formulario/" + "{autenticar}")
                .routeParam("autenticar", usuario)
                .asObject(String.class).getBody();
        System.out.println( "autenticar: " + repuesta);
        if (repuesta.matches("true")){
            return true;
        }
        else {
            return false;
        }

    }

    public static List<Formulario> listaFormulario(String usuario){


        HttpResponse<JsonNode> Listafull = Unirest.get("http://localhost:8043/api-Reset/formulario/listar").asJson();
        idGlobal = Listafull.getBody().getArray().length();
        try {
            List<Formulario> nuevosForms = Unirest.get("http://localhost:8043/api-Reset/formulario/listar-por-nombre/" + "{usuario}")
                    .routeParam("usuario", usuario)
                    .asObject(new GenericType<List<Formulario>>() {}).getBody();

            return nuevosForms;

        }catch (Exception e){
            System.out.println("Not Working List Forms");
        }

        return null;
    }

    public static void crearFormulario(String nombre, String sector, String nivelEscolar, String usuario,
                                       String longi, String lati, String base64, String mimetype, String filename){

        //verificacion
        System.out.println("Cosas que se van: " + nombre + "|" + sector + "|"+ nivelEscolar + "|" + usuario + "|" + mimetype + "|" + filename);
        //JSON FOTO
        JsonObject imgJSON = new JsonObject();
        imgJSON.addProperty("nombre", filename);
        imgJSON.addProperty("mimeType", mimetype);
        imgJSON.addProperty("fotoBase64", base64);

        //JSON OBJECT FORMULARIO
        JsonObject nuevoForm = new JsonObject();
        nuevoForm.addProperty("nombre", nombre);
        nuevoForm.addProperty("sector", sector);
        nuevoForm.addProperty("nivelEscolar", nivelEscolar);
        nuevoForm.addProperty("latitud", lati);
        nuevoForm.addProperty("longitud", longi);
        nuevoForm.addProperty("id", idGlobal + 1);
        nuevoForm.addProperty("usuario", usuario);
        nuevoForm.add("foto", imgJSON);


        HttpResponse<JsonNode> repuestaServidor = Unirest.post("http://localhost:8043/api-Reset/formulario/agregar")
                .header("Content-Type", "application/json")
                .body(nuevoForm)
                .asJson();

        //aumentando idGlobal
        idGlobal++;
        System.out.println("Nuevo Formulario: "+ repuestaServidor.getBody());
    }
}
