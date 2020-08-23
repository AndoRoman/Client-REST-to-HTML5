package Logic;

import java.util.Set;

public class Usuario {

    private String usuario;
    private String nombre;
    private String password;
    private Set<Roles> listaRoles;

    public Usuario(String usuario, String nombre, String password, Set<Roles> listaRoles) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
        this.listaRoles = listaRoles;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Roles> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(Set<Roles> listaRoles) {
        this.listaRoles = listaRoles;
    }
}
