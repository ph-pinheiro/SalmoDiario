package com.phpinheiro.salmodiario.model;

public class Salmo {

	private int id;
	private String numero;
	private String titulo;
	private String texto;
	private String data;
	private String trecho;

	public Salmo() {
	}

	public Salmo(String numero, String titulo, String texto, String data, String trecho) {
		super();
		this.numero = numero;
		this.titulo = titulo;
		this.texto = texto;
		this.data = data;
		this.trecho = trecho;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTrecho() {
		return trecho;
	}

	public void setTrecho(String trecho) {
		this.trecho = trecho;
	}
}