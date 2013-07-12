package com.lightsaber;

public interface OnSaberAction
{
	void onSaberOpen();  // I know this is an awful name, but I couldn't think of a better one :-/
	void onSaberClose(); // I know this is an awful name, but I couldn't think of a better one :-/
	void onSaberHit();
	void onSaberMove();
}
