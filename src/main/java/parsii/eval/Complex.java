/*
 * Chaos - simple 2D iterated function system plotter and editor.
 * Copyright (C) 2021 YouZhe Zhen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Abdul Fatir
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * <code>ComplexNumber</code> is a class which implements complex numbers in Java. 
 * It includes basic operations that can be performed on complex numbers such as,
 * addition, subtraction, multiplication, conjugate, modulus and squaring. 
 * The data type for Complex Numbers.
 * <br /><br />
 * The features of this library include:<br />
 * <ul>
 * <li>Arithmetic Operations (addition, subtraction, multiplication, division)</li>
 * <li>Complex Specific Operations - Conjugate, Inverse, Absolute/Magnitude, Argument/Phase</li>
 * <li>Trigonometric Operations - sin, cos, tan, cot, sec, cosec</li>
 * <li>Mathematical Functions - exp</li>
 * <li>Complex Parsing of type x+yi</li>
 * </ul>
 * 
 * @author      Abdul Fatir
 * @version		1.2
 * 
 */
package parsii.eval;

/**
 * @author YouZhe Zhen
 *
 */
public class Complex extends Number implements Comparable<Complex> {

	private static final long serialVersionUID = -689992285746814441L;

	public static final Complex ZERO = new Complex(0, 0);
	
	public static final Complex FALSE = ZERO;
	
	public static final Complex TRUE = new Complex(1, 1);

	public static final Complex NaN = new Complex(Double.NaN, Double.NaN);

	public static final Complex PI = new Complex(Math.PI, 0.0);

	public static final Complex E = new Complex(Math.E, 0.0);

	private double real;
	private double image;

	public Complex(Complex clone) {
		this.real = clone.real;
		this.image = clone.image;
	}

	public Complex(double real, double image) {
		this.real = real;
		this.image = image;
	}

	public double abs() {
		return Math.hypot(real, image);
	}

	public Complex mul(Complex other) {
		double real = this.real * other.real - this.image * other.image;
		double image = this.real * other.image + this.image * other.real;
		return new Complex(real, image);
	}

	public Complex add(Complex other) {
		return new Complex(real + other.real, image + other.image);
	}

	public Complex sub(Complex other) {
		return new Complex(real - other.real, image - other.image);
	}

	public Complex div(Complex other) {
		// FIXME
		double real = this.real / other.real - this.image / other.image;
		double image = this.real / other.image + this.image / other.real;
		return new Complex(real, image);
	}
	
	public Complex pow(Complex other) {
		// FIXME
		return null;
	}

	/**
	 * @param b
	 * @return
	 */
	public Complex mod(Complex b) {
		// TODO Auto-generated method stub
		return null;
	}

	public Complex sqrt() {
		double r = Math.sqrt(this.real * this.real + this.image * this.image);
		double real = Math.sqrt((r + this.real) / 2);
		double image = Math.sqrt((r - this.real) / 2);
		return new Complex(real, image);
	}

	public Complex toRadians() {
		return new Complex(Math.toRadians(real), Math.toRadians(image));
	}

	public Complex toDegrees() {
		return new Complex(Math.toDegrees(real), Math.toDegrees(image));
	}

	public Complex signum() {
		return new Complex(real > 0 ? 1.0 : -1.0, image > 0 ? 1.0 : -1.0);
	}

	/**
	 * Returns the real.
	 * 
	 * @return the real
	 */
	public double getReal() {
		return real;
	}

	/**
	 * @param real the real to set
	 */
	public void setReal(double real) {
		this.real = real;
	}

	/**
	 * Returns the image.
	 * 
	 * @return the image
	 */
	public double getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(double image) {
		this.image = image;
	}

	public static Complex valueOf(boolean b) {
		return b ? TRUE : FALSE;
	}


	public static Complex max(Complex a, Complex b) {
		return a.abs() >= b.abs() ? a : b;
	}


	public boolean isNaN() {
		return Double.isNaN(real) || Double.isNaN(image);
	}

	@Override
	public int intValue() {
		return (int) abs();
	}

	@Override
	public long longValue() {
		return (long) abs();
	}

	@Override
	public float floatValue() {
		return (float) abs();
	}

	@Override
	public double doubleValue() {
		return abs();
	}

	public boolean toBoolean() {
		return !equals(FALSE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(image);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(real);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Complex)) {
			return false;
		}
		Complex other = (Complex) obj;
		if (Double.doubleToLongBits(image) != Double
				.doubleToLongBits(other.image)) {
			return false;
		}
		if (Double.doubleToLongBits(real) != Double
				.doubleToLongBits(other.real)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return real + (image < 0 ? "-" : "+" ) + image + "i";
	}

	@Override
	public int compareTo(Complex o) {
		return (abs() < o.abs()) ? -1 : ((abs() == o.abs()) ? 0 : 1);
	}

	/**
	 * @return
	 */
	public Complex log() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex log10() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex exp() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	public static Complex min(Complex a, Complex b) {
		return a.abs() <= b.abs() ? a : b;
	}

	/**
	 * @return
	 */
	public Complex ceil() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex floor() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex round() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex atan() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex acos() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex asin() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex sin() {
		double x = Math.exp(image);
		double x_inv = 1 / x;
		double r = Math.sin(real) * (x + x_inv) / 2;
		double i = Math.cos(real) * (x - x_inv) / 2;
		return new Complex(r, i);
	}

	/**
	 * @return
	 */
	public Complex sinh() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex cos() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex cosh() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex tan() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Complex tanh() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	public static Complex atan2(Complex a, Complex b) {
		// TODO Auto-generated method stub
		return null;
	}

}
