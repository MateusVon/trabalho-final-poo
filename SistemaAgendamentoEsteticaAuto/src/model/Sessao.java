package model;

public class Sessao {
  private static Cliente usuarioLogado;

  public static void setUsuarioLogado(Cliente usuario) {
    usuarioLogado = usuario;
  }

  public static Cliente getUsuarioLogado() {
    return usuarioLogado;
  }

  public static void logout() {
    usuarioLogado = null;
  }
}
