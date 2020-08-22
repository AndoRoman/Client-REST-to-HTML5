package visual;

import Logic.ClientREST;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import java.io.IOException;
import java.util.*;
import org.apache.commons.io.IOUtils;;

public class ControladorPlantilla {

    public ControladorPlantilla() {
        registrarPlantilla();
    }

    public void registrarPlantilla() {
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }

    public void rutas(Javalin app) {
        app.routes(() -> {

            //Vista del Login
            app.get("/", ctx -> {

                ctx.render("/login.html");
            });


            //Vista para registrar nuevo formulario
            app.get("/crear", ctx -> {

                if (ctx.sessionAttribute("usuario") != null) {

                    ctx.render("/register.html");

                }else{
                    ctx.redirect("/");
                }

            });


            //REGISTRAR formulario
            app.post("/registrar", ctx -> {
                Map<String, Object> modelo = new HashMap<>();

                String nombre = ctx.formParam("nombre");
                String sector = ctx.formParam("sector");
                String lati = ctx.formParam("lati");
                String longi = ctx.formParam("longi");
                String nivelEscolar = ctx.formParam("nivelEscolar");

                String usuario = ctx.sessionAttribute("usuario");

;

                ctx.uploadedFiles("thefiles").forEach(uploadedFile -> {
                    System.out.println("\n CARGANDO IMAGEN!! \n");
                    try {
                        byte[] bytes = IOUtils.toByteArray(uploadedFile.getContent());
                        String encodedString = Base64.getEncoder().encodeToString(bytes);

                        String filename = uploadedFile.getFilename();
                        String mimetype = uploadedFile.getContentType();
                        //call function
                        ClientREST.crearFormulario(nombre, sector, nivelEscolar, usuario, longi, lati, encodedString, mimetype, filename);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });





            });

            //VISTA principal
            app.get("/dashboard", ctx -> {

                //VERIFICAR SI EXISTE COOKIE PARA ENTRAR A LA PAGINA PRINCIPAL O LLEVAR AL LOGIN
                if (ctx.sessionAttribute("usuario") != null) {


                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("user", "Formularios creados por " + ctx.sessionAttribute("usuario"));
                    modelo.put("formularios", ClientREST.listaFormulario()); //<-- ENVIAR forms de USUARIO CORRESPONDIENTE
                    ctx.render("/index.html", modelo);
                } else {
                    ctx.redirect("/login");
                }
            });

            //CERRAR SESION
            app.get("/loginOUT", ctx -> {
                ctx.clearCookieStore();
                ctx.sessionAttribute("usuario", null);

                ctx.redirect("/");
            });


            //AUTENTICACIÓN EN EL LOGIN
            app.post("/signin", ctx -> {
                Map<String, Object> modelo = new HashMap<>();
                String user = ctx.formParam("user");
                String pass = ctx.formParam("pass");

                if (user.matches("admin") && pass.matches("admin")) {
                    //creando un atributo de sesion
                    ctx.sessionAttribute("usuario", user);
                    //PAGINA PRINCIPAL
                    ctx.redirect("/dashboard");
                } else {
                    ctx.redirect("/");
                }

            });

        });
    }
}