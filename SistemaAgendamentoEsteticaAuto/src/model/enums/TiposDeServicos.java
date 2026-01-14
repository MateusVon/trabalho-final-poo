package model.enums;

public enum TiposDeServicos {
  // Serviços gerais (Aplicados a qualquer categoria de veículo, exceto insulfim
  // que não se aplica a motos).
  LAVAGEM_SIMPLES(false),
  LAVAGEM_DETALHADA(false),
  POLIMENTO_TECNICO(false),
  PPF(false),
  INSULFILM(false),
  DESCONTAMINACAO_PINTURA(false),
  RENOVACAO_ESTOFADOS(false),

  // Ao declarar uma constante Enum com false/true, está sendo chamado o
  // construtor implicitamente para atribuir o valor do atributo
  // "exclusivoTransporte", sendo este valor imutável pelo usuário devido o
  // atributo ser declarado como final.

  // Serviços exclusivos para veículos de transporte.
  LIMPEZA_DA_QUINTA_RODA(true),
  LAVAGEM_CARRETA(true),
  POLIMENTO_TECNICO_CARRETA(true);

  private final boolean exclusivoTransporte;

  TiposDeServicos(boolean exclusivoTransporte) {
    this.exclusivoTransporte = exclusivoTransporte;
  }

  public boolean isExclusivoTransporte() {
    return exclusivoTransporte;
  }

}
