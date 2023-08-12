package jokenpo;

public enum Jogada {

    PEDRA("PEDRA","TESOURA"),
    PAPEL("PAPEL", "PEDRA"),
    TESOURA("TESOURA", "PAPEL");

	private String nome;
    private String ganhaDe;

    Jogada(String nome, String ganhaDe) {
    	this.nome = nome;
        this.ganhaDe = ganhaDe;
    }

    public String getNome() {
    	return this.nome;
    }
    
	public boolean ganhaDe(Jogada jogada) {
		return this.ganhaDe.equals(jogada.nome);
	}
	
	public static Jogada getJogada(String jogada) {
		if(jogada.equals(PAPEL.nome)) 
			return PAPEL;
		if(jogada.equals(TESOURA.nome))
			return TESOURA;
		if(jogada.equals(PEDRA.nome))
			return PEDRA;
		return null;
	}
}