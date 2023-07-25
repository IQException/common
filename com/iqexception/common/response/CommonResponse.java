package com.iqexception.tool.response;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.List;
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
public class CommonResponse implements com.ctriposs.baiji.rpc.common.HasResponseStatus, Serializable {
    private static final long serialVersionUID = 1L;
    public CommonResponse() {}
    public CommonResponse(
            com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus) {
        this.responseStatus = responseStatus;
    }
    @JsonProperty(value = "responseStatus", index = 1)
    private com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus;
    public com.ctriposs.baiji.rpc.common.types.ResponseStatusType getResponseStatus() {
        return responseStatus;
    }
    public void setResponseStatus(com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus) {
        this.responseStatus = responseStatus;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommonResponse other = (CommonResponse)obj;
        return
                Objects.equal(this.responseStatus, other.responseStatus);
    }
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Objects.hashCode(this.responseStatus);
        return result;
    }
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("responseStatus", responseStatus)
                .toString();
    }
    private boolean listArrayEquals(List<byte[]> thisValue, List<byte[]> otherValue) {
        if (thisValue == null && otherValue == null) {
            return true;
        }
        if (thisValue != null && otherValue != null) {
            if (thisValue.size() != otherValue.size()) {
                return false;
            } else {
                java.util.Iterator<byte[]> thisIter = thisValue.iterator();
                java.util.Iterator<byte[]> otherIter = otherValue.iterator();
                while (thisIter.hasNext()) {
                    byte[] thisArray = thisIter.next();
                    byte[] otherArray = otherIter.next();
                    if (!java.util.Arrays.equals(thisArray, otherArray)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    private int listArrayHashCode(List<byte[]> value) {
        if (value == null) {
            return 0;
        } else {
            int result = 1;
            for (byte[] array : value) {
                result = 31 * result + java.util.Arrays.hashCode(array);
            }
            return result;
        }
    }
}
