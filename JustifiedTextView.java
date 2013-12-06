import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;

public class JustifiedTextView extends View {
	String text;
	ArrayList<Line> linesCollection = new ArrayList<Line>();
	TextPaint textPaint;
	Typeface font;
	int textColor;
	float textSize = 42f, lineHeight = 57f, wordSpacing = 15f, lineSpacing = 15f;
	float onBirim, w, h;
	float leftPadding, rightPadding;

	public JustifiedTextView(Context context, String text) {
		super(context);
		this.text = text;
		init();
	}

	private void init() {
		textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textColor = Color.BLACK;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (font != null) {
			font = Typeface.createFromAsset(getContext().getAssets(), "font/Trykker-Regular.ttf");
			textPaint.setTypeface(font);
		}
		textPaint.setColor(textColor);

		int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
		w = resolveSizeAndState(minw, widthMeasureSpec, 1);
		h = MeasureSpec.getSize(widthMeasureSpec);

		onBirim = 0.009259259f * w;
		lineHeight = textSize + lineSpacing;
		leftPadding = 3 * onBirim + getPaddingLeft();
		rightPadding = 3 * onBirim + getPaddingRight();

		textPaint.setTextSize(textSize);

		wordSpacing = 15f;
		Line lineBuffer = new Line();
		this.linesCollection.clear();
		String[] lines = text.split("\n");
		for (String line : lines) {
			String[] words = line.split(" ");
			lineBuffer = new Line();
			float lineWidth = leftPadding + rightPadding;
			float totalWordWidth = 0;
			for (String word : words) {
				float ww = textPaint.measureText(word) + wordSpacing;
				if (lineWidth + ww + (lineBuffer.getWords().size() * wordSpacing) > w) {// is
					lineBuffer.addWord(word);
					totalWordWidth += textPaint.measureText(word);
					lineBuffer.setSpacing((w - totalWordWidth - leftPadding - rightPadding) / (lineBuffer.getWords().size() - 1));
					this.linesCollection.add(lineBuffer);
					lineBuffer = new Line();
					totalWordWidth = 0;
					lineWidth = leftPadding + rightPadding;
				} else {
					lineBuffer.setSpacing(wordSpacing);
					lineBuffer.addWord(word);
					totalWordWidth += textPaint.measureText(word);
					lineWidth += ww;
				}
			}
			this.linesCollection.add(lineBuffer);
		}
		setMeasuredDimension((int) w, (int) ((this.linesCollection.size() + 1) * lineHeight + (10 * onBirim)));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawLine(0f, 10f, getMeasuredWidth(), 10f, textPaint);
		float x, y = lineHeight + onBirim;
		for (Line line : linesCollection) {
			x = leftPadding;
			for (String s : line.getWords()) {
				canvas.drawText(s, x, y, textPaint);
				x += textPaint.measureText(s) + line.spacing;
			}
			y += lineHeight;
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Typeface getFont() {
		return font;
	}

	public void setFont(Typeface font) {
		this.font = font;
	}

	public float getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
	}

	public float getLeftPadding() {
		return leftPadding;
	}

	public void setLeftPadding(float leftPadding) {
		this.leftPadding = leftPadding;
	}

	public float getRightPadding() {
		return rightPadding;
	}

	public void setRightPadding(float rightPadding) {
		this.rightPadding = rightPadding;
	}

	public void setWordSpacing(float wordSpacing) {
		this.wordSpacing = wordSpacing;
	}

	public float getWordSpacing() {
		return wordSpacing;
	}

	public float getLineSpacing() {
		return lineSpacing;
	}

	public void setLineSpacing(float lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

	class Line {
		ArrayList<String> words = new ArrayList<String>();
		float spacing = 15f;

		public Line() {
		}

		public Line(ArrayList<String> words, float spacing) {
			this.words = words;
			this.spacing = spacing;
		}

		public void setSpacing(float spacing) {
			this.spacing = spacing;
		}

		public float getSpacing() {
			return spacing;
		}

		public void addWord(String s) {
			words.add(s);
		}

		public ArrayList<String> getWords() {
			return words;
		}
	}
}
