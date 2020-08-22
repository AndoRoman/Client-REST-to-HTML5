package Logic;

import com.google.gson.JsonObject;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import kong.unirest.HttpResponse;
import java.util.Scanner;

public class ClientREST {

    private static int idGlobal;

    private static Scanner entradaEscaner= new Scanner (System.in);
    private static String URLApi = "http://localhost:7000/api/estudiante/";

    public static void consultarEstudiante(String matricula){
        System.out.println("Consultado Estudiante con la matricula: " + matricula + "\n");

        HttpResponse<JsonNode> repuestaServidor = Unirest.get(URLApi+"{matricula}")
                .routeParam("matricula", matricula)
                .asJson();

        System.out.println("Repuesta del Servidor: \n"+repuestaServidor.getBody()+"\n");
    }

    public static java.util.Map<String, Object> listaFormulario(){
        HttpResponse<JsonNode> repuestaServidor = Unirest.get(URLApi).asJson();
        idGlobal = repuestaServidor.getBody().getArray().length();
        return repuestaServidor.getBody().getObject().toMap();
    }

    public static void crearFormulario(String nombre, String sector, String nivelEscolar, String usuario,
                                       String longi, String lati, String base64, String mimetype, String filename){

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
        nuevoForm.addProperty("id", idGlobal);
        nuevoForm.addProperty("usuario", usuario);
        nuevoForm.add("foto", imgJSON);


        HttpResponse<JsonNode> repuestaServidor = Unirest.post(URLApi)
                .header("Content-Type", "application/json")
                .body(nuevoForm)
                .asJson();

        //aumentando idGlobal
        idGlobal++;
        System.out.println("Nuevo Formulario: \n"+repuestaServidor.getBody()+"\n");
    }
}
