package com.github.niwaniwa.whitebird.pvp.util.message;

public enum LanguageType {

	en_US("en_US"),
	en_GB("en_GB"),
	ja_JP("ja_JP");

	public static final LanguageType defaultLang = ja_JP;

	private String lang;

	private LanguageType(final String lang) {
        this.lang = lang;
    }

	public String getLang(){
		return lang;
	}

}
