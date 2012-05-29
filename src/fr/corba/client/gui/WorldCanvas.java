package fr.corba.client.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import fr.corba.idl.Code.Piece;

public class WorldCanvas extends Canvas {
	static final int sizeX = 3;
	static final int sizeY = 3;
	private Piece piece;

	public void setPiece(Piece p) {
		if (p.posX < sizeX && p.posY < sizeY) {
			piece = p;
		}
	}

	public void changePiece(Piece p) {
		if (p.posX < sizeX && p.posY < sizeY) {
			piece = p;
			update(this.getGraphics());
		}
	}

	public void paint(Graphics g) {
		this.setSize(new Dimension(400, 340));
		int nbX = 3, nbY = 3;
		int rectWitdh = 50, rectLength = 50;
		int posX, posY = (this.getHeight() - (rectLength * nbY)) / 2;

		for (int y = 0; y < 3; y++) {
			posX = (this.getWidth() - (rectWitdh * nbX)) / 2;
			for (int x = 0; x < 3; x++) {
				// Les bordures sont noires
				g.setColor(Color.black);
				g.drawRect(posX, posY, rectWitdh, rectLength);
				if (piece.posX == x && piece.posY == y) {
					// Le contenu est vert
					g.setColor(Color.green);
					g.fillRect(posX + 1, posY + 1, rectWitdh - 1, rectLength - 1);
				}
				posX += rectWitdh;
			}
			posY += rectLength;
		}
	}
}
