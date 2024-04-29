package labs.lab3_game;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MyUtils {
  public static final byte byteToBool(boolean val) {
    return val ? (byte)1 : (byte)0;
  }

  public static final boolean boolToByte(byte val) {
    return val != (byte)0 ? true : false;
  }

  public static class ArrowState {
    public boolean visible;
    public double posX;

    ArrowState() {
      visible = false;
      posX = 0.0;
    }

    ArrowState(boolean visible, double pos) {
      this.visible = visible;
      this.posX = pos;
    }

    ArrowState(byte[] bytes) {
      this(ByteBuffer.wrap(bytes));
    }

    ArrowState(ByteBuffer buffer) {
      visible = boolToByte(buffer.get());
      posX = buffer.getDouble();
    }

    public byte[] generateBytes() {
      byte[] bytes = new byte[9];
      ByteBuffer buffer = ByteBuffer.wrap(bytes);
      buffer.put(byteToBool(visible));
      buffer.putDouble(posX);
      return bytes;
    }
  }

  public static class ArrowStateArray {
    public ArrowState[] arr;

    ArrowStateArray(int n) {
      arr = new ArrowState[n];
      for (int i = 0; i < n; i++) {
        arr[i] = new ArrowState();
      }
    }

    ArrowStateArray(byte[] bytes, int offset, int length) {
      this((length <= bytes.length ? length : bytes.length) / 9);
      for (int i = 0; i < arr.length; i++) {
        arr[i] = new ArrowState(ByteBuffer.wrap(bytes, offset+i*9, 9));
      }
    }

    public byte[] generateBytes() {
      byte[] bytes = new byte[9*arr.length];
      for (int i = 0; i < arr.length; i++) {
        System.arraycopy(arr[i].generateBytes(), 0, bytes, i*9, 9);
      }
      return bytes;
    }
  }

  public static class PlayerWins {
    public final static int bytes_size = Config.name_max_length + 4;
    int wins;
    String name;
    
    public PlayerWins() {}

    public PlayerWins(int wins, String name) {
      this.wins = wins;
      this.name = name;
    }

    public PlayerWins(ByteBuffer buffer) {
      this.wins = buffer.getInt();
      byte[] nameBuffer = new byte[buffer.remaining()];
      buffer.get(nameBuffer);
      this.name = new String(nameBuffer, StandardCharsets.UTF_8);
    }

    public byte[] generateBytes() {
      byte[] bytes = new byte[bytes_size];
      ByteBuffer buffer = ByteBuffer.wrap(bytes);
      buffer.putInt(wins);
      buffer.put(name.getBytes());
      return bytes;
    }
  }

  public static class PlayerWinsArray {
    public PlayerWins[] arr;

    PlayerWinsArray(int n) {
      arr = new PlayerWins[n];
      for (int i = 0; i < n; i++) {
        arr[i] = new PlayerWins();
      }
    }

    PlayerWinsArray(byte[] bytes, int offset, int length) {
      this((length <= bytes.length ? length : bytes.length) / PlayerWins.bytes_size);
      for (int i = 0; i < arr.length; i++) {
        arr[i] = new PlayerWins(ByteBuffer.wrap(bytes, offset + i * PlayerWins.bytes_size, PlayerWins.bytes_size));
      }
    }

    public byte[] generateBytes() {
      byte[] bytes = new byte[arr.length * PlayerWins.bytes_size];
      for (int i = 0; i < arr.length; i++) {
        System.arraycopy(arr[i].generateBytes(), 0, bytes, i * PlayerWins.bytes_size, PlayerWins.bytes_size);
      }
      return bytes;
    }
  }
}
