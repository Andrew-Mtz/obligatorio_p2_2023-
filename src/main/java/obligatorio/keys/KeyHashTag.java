package obligatorio.keys;

public class KeyHashTag {  // Debo override el hashCode y el equals cuando my key es un string para asegurarme que no de valores negativos

    private String hashTagKey;

    public KeyHashTag(String hashTagKey){
        this.hashTagKey = hashTagKey;
    }

    public String getHashTagKey() {
        return hashTagKey;
    }

    public void setHashTagKey(String hashTagKey) {
        this.hashTagKey = hashTagKey;
    }

    @Override
    public int hashCode(){
        int code = 3;
        for (int i = 0; i < this.hashTagKey.length(); i++) {
            code = (code * 31) + this.hashTagKey.charAt(i);
        }
        return Math.abs(code);
    }

    @Override
    public boolean equals (Object lengCodeNew){
        if (lengCodeNew instanceof KeyHashTag){
            KeyHashTag temp = (KeyHashTag) lengCodeNew;
            if (this.hashTagKey.equals(temp.getHashTagKey())){
                return true;
            }
        }
        return false;
    }

}
