package com.example.models;

import quickdt.data.Attributes;
import quickdt.data.HashMapAttributes;

public class Pixel {
	public int h;
	public int s;
	public int v;
	public Pixel(int inh, int ins, int inv){
		h=inh;
		s=ins;
		v = inv;
	}
	public String toString()
	{
		return h+","+s+","+v;
	}
	public Attributes getAttributes()
	{
		return HashMapAttributes.create("h",
				String.valueOf(h), "s", String.valueOf(s), "v",
				String.valueOf(v));
	}
}
