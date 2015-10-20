package im.actor.core.api.rpc;
/*
 *  Generated by the Actor API Scheme generator.  DO NOT EDIT!
 */

import im.actor.runtime.bser.*;
import im.actor.runtime.collections.*;
import static im.actor.runtime.bser.Utils.*;
import im.actor.core.network.parser.*;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.google.j2objc.annotations.ObjectiveCName;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import im.actor.core.api.*;

public class RequestGetFileUploadUrl extends Request<ResponseGetFileUploadUrl> {

    public static final int HEADER = 0x61;
    public static RequestGetFileUploadUrl fromBytes(byte[] data) throws IOException {
        return Bser.parse(new RequestGetFileUploadUrl(), data);
    }

    private int expectedSize;

    public RequestGetFileUploadUrl(int expectedSize) {
        this.expectedSize = expectedSize;
    }

    public RequestGetFileUploadUrl() {

    }

    public int getExpectedSize() {
        return this.expectedSize;
    }

    @Override
    public void parse(BserValues values) throws IOException {
        this.expectedSize = values.getInt(1);
    }

    @Override
    public void serialize(BserWriter writer) throws IOException {
        writer.writeInt(1, this.expectedSize);
    }

    @Override
    public String toString() {
        String res = "rpc GetFileUploadUrl{";
        res += "expectedSize=" + this.expectedSize;
        res += "}";
        return res;
    }

    @Override
    public int getHeaderKey() {
        return HEADER;
    }
}