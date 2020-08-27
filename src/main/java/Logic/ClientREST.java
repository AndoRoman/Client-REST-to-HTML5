package Logic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kong.unirest.GenericType;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import kong.unirest.HttpResponse;

import java.util.List;

public class ClientREST {
    /**
     * Referencia: https://www.adictosaltrabajo.com/2012/09/17/gson-java-json/
     */

    private static int idGlobal;
    private  static String token;
    private static String URLApi = "http://localhost:8043/api-Rest/formulario";
    private static RepuestaLogin repuestaLogin=null;

    public static boolean consultarUsuario(String usuario){
        HttpResponse<String> repuestaServidor = Unirest.get("http://localhost:8043/api-Rest/" + "{autenticar}")
                .routeParam("autenticar", usuario)
                .asString();
        Gson gson = new Gson();
        repuestaLogin = gson.fromJson(repuestaServidor.getBody(), RepuestaLogin.class);
        System.out.println("Token recibido... "+repuestaLogin.getToken());
        if (repuestaLogin.getToken()!=null){
            return true;
        }
        return false;

    }
    public static List<Formulario> listaFormulario(String usuario){
        try {
            List<Formulario> nuevosForms = Unirest.get("http://localhost:8043/api-Rest/formulario/listar-por-nombre/" + "{usuario}")
                    .routeParam("usuario", usuario)
                    .header("Authorization",repuestaLogin.getToken())
                    .asObject(new GenericType<List<Formulario>>() {}).getBody();
            idGlobal = nuevosForms.size();
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
        nuevoForm.addProperty("id", usuario+"-"+idGlobal + 1);
        nuevoForm.addProperty("usuario", usuario);
        nuevoForm.add("foto", imgJSON);

        HttpResponse<JsonNode> repuestaServidor = Unirest.post("http://localhost:8043/api-Rest/formulario/agregar/")
                .header("Content-Type", "application/json")
                .header("Authorization",repuestaLogin.getToken())
                .body(nuevoForm)
                .asJson();
        idGlobal++;
        System.out.println("Nuevo Formulario: "+ repuestaServidor.getBody());
    }
}
