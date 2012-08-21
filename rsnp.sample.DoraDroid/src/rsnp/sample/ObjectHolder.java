/*
 * $Id: ObjectHolder.java 185 2010-04-22 05:38:47Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited. FUJITSU CONFIDENTIAL.
 */
package rsnp.sample;

import java.util.Hashtable;

/**
 * 登録したオブジェクトをどのクラスでも取得できるようにするホルダー
 *
 */
public class ObjectHolder {

	/** 唯一のオブジェクト */
	private static ObjectHolder own = new ObjectHolder();
	/** ビューを保持するマップ */
	private Hashtable<String, Object> map = new Hashtable<String, Object>();

	/**
	 * 唯一のインスタンスを取得する
	 *
	 * @return 唯一のViewHolderオブジェクト
	 */
	public static ObjectHolder getInstance() {
		return own;
	}

	/**
	 * デフォルトコンストラクタ
	 */
	private ObjectHolder() {

	}

	/**
	 * 保持するビューを追加
	 *
	 * @param name
	 *            ビューの名前
	 * @param view
	 *            保持するビューオブジェクト
	 */
	public <T> void add(String name, T view) {

		map.put(name, view);
	}

	/**
	 * 指定された名前のビューを取得する
	 *
	 * @param name
	 *            ビューの名前
	 * @return 取得するビューオブジェクト
	 */
	public <T> T get(String name) {

		return (T) map.get(name);
	}
}
