package rlib.util.ref;

/**
 * Ссылка на тип данных char.
 * 
 * @author Ronn
 */
final class CharReference extends AbstractReference {

	/** значение по ссылке */
	private char value;

	@Override
	public char getChar() {
		return value;
	}

	@Override
	public ReferenceType getReferenceType() {
		return ReferenceType.CHAR;
	}

	@Override
	public void setChar(final char value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CharReference [value=" + value + "]";
	}
}