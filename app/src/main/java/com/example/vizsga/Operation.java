package com.example.vizsga;

import android.os.Parcel;
import android.os.Parcelable;

public class Operation implements Parcelable {
    int a;
    int b;
    String op;

    public Operation(int a, int b, String op) {
        this.a = a;
        this.b = b;
        this.op = op;
    }

    protected Operation(Parcel in) {
        a = in.readInt();
        b = in.readInt();
        op = in.readString();
    }

    public static final Creator<Operation> CREATOR = new Creator<Operation>() {
        @Override
        public Operation createFromParcel(Parcel in) {
            return new Operation(in);
        }

        @Override
        public Operation[] newArray(int size) {
            return new Operation[size];
        }
    };

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public String getOp() {
        return op;
    }

    @Override
    public String toString() {
        return a + " " + op + " " + b;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(a);
        dest.writeInt(b);
        dest.writeString(op);
    }
}
