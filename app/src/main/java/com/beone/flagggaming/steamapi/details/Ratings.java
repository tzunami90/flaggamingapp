package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class Ratings{

	@SerializedName("esrb")
	private Esrb esrb;

	@SerializedName("pegi")
	private Pegi pegi;

	@SerializedName("steam_germany")
	private SteamGermany steamGermany;

	@SerializedName("dejus")
	private Dejus dejus;

	@SerializedName("oflc")
	private Oflc oflc;

	@SerializedName("nzoflc")
	private Nzoflc nzoflc;

	@SerializedName("usk")
	private Usk usk;

	@SerializedName("kgrb")
	private Kgrb kgrb;

	@SerializedName("csrr")
	private Csrr csrr;

	public Esrb getEsrb(){
		return esrb;
	}

	public Pegi getPegi(){
		return pegi;
	}

	public SteamGermany getSteamGermany(){
		return steamGermany;
	}

	public Dejus getDejus(){
		return dejus;
	}

	public Oflc getOflc(){
		return oflc;
	}

	public Nzoflc getNzoflc(){
		return nzoflc;
	}

	public Usk getUsk(){
		return usk;
	}

	public Kgrb getKgrb(){
		return kgrb;
	}

	public Csrr getCsrr(){
		return csrr;
	}
}