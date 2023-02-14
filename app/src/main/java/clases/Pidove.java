package clases;

import java.io.Serializable;

public class Pidove implements Serializable{

	private static final long serialVersionUID = 1L;
	public int action;
	public Usuario usuario;
	public String data;


	public Pidove(int action, Usuario usuario, String data) {
		super();
		this.action = action;
		this.usuario = usuario;
		this.data  = data;
	}

}