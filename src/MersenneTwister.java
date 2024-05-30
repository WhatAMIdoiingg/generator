import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MersenneTwister {
    private static final int MersenneExponent = 19937; // Константа, определяющая размер генератора Мерсенна
    private static final int Length128 = MersenneExponent / 128 + 1; // Длина массива состояния в 128-битных целых числах
    private static final int Length32 = Length128 * 4; // Длина массива состояния в 32-битных целых числах

    private int[] state = new int[Length32]; // Массив состояния генератора
    private int index; // Текущий индекс в массиве состояния

    private static MersenneTwister shared; // Общий экземпляр генератора

    public static MersenneTwister getShared() {
        // Если общий экземпляр не создан, создаем его с случайным сидом
        if (shared == null) {
            shared = new MersenneTwister(getRandomSeed());
        }
        return shared;
    }



    public MersenneTwister(int seed) {
        // Создаем генератор с заданным сидом
        initGenerator(seed);
    }

    private MersenneTwister(long seed) {
        // Создаем генератор с заданным сидом, приведенным к int
        initGenerator((int)seed);
    }

    private static int getRandomSeed() {
        // Генерируем случайный сид, используя SHA-256 хеш случайного байтового массива
        Random rand = new Random();
        byte[] bytes = new byte[16];
        rand.nextBytes(bytes);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = md.digest(bytes);
        int result = 0;
        for (int i = 0; i < hash.length; i += 4) {
            result ^= ((hash[i] & 0xFF) << 24) | ((hash[i + 1] & 0xFF) << 16) | ((hash[i + 2] & 0xFF) << 8) | (hash[i + 3] & 0xFF);
        }
        return result;
    }

    private void initGenerator(int seed) {
        // Инициализируем генератор с заданным сидом
        state[0] = seed;
        for (int i = 1; i < Length32; i++) {
            state[i] = (int)(1812433253L * (state[i - 1] ^ (state[i - 1] >> 30)) + i);
        }
        index = Length32;
        periodCertification();
    }

    private void periodCertification() {
        // Проверяем период генератора
        int inner = 0;
        int[] parity = {0x00000001, 0x00000000, 0x00000000, 0x13c9e684};

        for (int i = 0; i < 4; i++) {
            inner ^= state[i] & parity[i];
        }
        for (int i = 16; i > 0; i >>= 1) {
            inner ^= inner >> i;
        }
        inner &= 1;

        if (inner == 1) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            for (int work = 1; work != 0; work <<= 1) {
                if ((work & parity[i]) != 0) {
                    state[i] ^= work;
                    return;
                }
            }
        }
    }

    private void updateState() {
        // Обновляем состояние генератора
        int offset = 122;
        int i;
        int r1 = state[Length32 - 2];
        int r2 = state[Length32 - 1];
        for (i = 0; i < Length32 - offset; i++) {
            state[i] = doRecursion(state[i], state[i + offset], r1, r2);
            r1 = r2;
            r2 = state[i];
        }
        for (; i < Length32; i++) {
            state[i] = doRecursion(state[i], state[i + offset - Length32], r1, r2);
            r1 = r2;
            r2 = state[i];
        }
    }

    private int doRecursion(int a, int b, int c, int d) {
        // Выполняем рекурсивный шаг генерации
        int z = c >>> 1;
        z ^= a;
        int v = d << 18;
        z ^= v;
        int x = a << 1;
        z ^= x;
        int y = b >> 11;
        int mask = 0xdfffffef;
        y &= mask;
        return z ^ y;
    }

    public long nextUInt64() {
        // Генерируем следующее 64-битное целое число без знака
        if (index >= Length32) {
            updateState();
            index = 0;
        }
        long r = (long)state[index / 2] & 0xFFFFFFFFL;
        index += 2;
        return r;
    }

    public int nextUInt32() {
        // Генерируем следующее 32-битное целое число без знака
        if (index >= Length32) {
            updateState();
            index = 0;
        }
        return state[index++];
    }

    public int next() {
        // Генерируем следующее целое число в диапазоне [0, Integer.MAX_VALUE)
        return (int)(((long)Integer.MAX_VALUE * nextUInt32()) >> 32);
    }

    public int next(int maxValue) {
        // Генерируем следующее целое число в диапазоне [0, maxValue)
        if (maxValue <= 0) {
            throw new IllegalArgumentException("Value must be greater than 0.");
        }
        return (int)(((long)maxValue * nextUInt32()) >> 32);
    }

    public int next(int minValue, int maxValue) {
        // Генерируем следующее целое число в диапазоне [minValue, maxValue)
        if (minValue < 0) {
            throw new IllegalArgumentException("Value must be greater than or equal to 0.");
        }
        if (maxValue <= minValue) {
            throw new IllegalArgumentException("Value must be greater than minValue.");
        }
        return (int)(((long)(maxValue - minValue) * nextUInt32()) >> 32) + minValue;
    }

    public long nextInt64(long minValue, long maxValue) {
        if (minValue < 0) {
            throw new IllegalArgumentException("Value must be greater than or equal to 0.");
        }
        if (maxValue <= minValue) {
            throw new IllegalArgumentException("Value must be greater than minValue.");
        }
        return (long)(((long)(maxValue - minValue) * nextUInt64()) >> 64) + minValue;
    }

    public double nextDouble() {
        return (nextUInt64() >> 11) / 9007199254740992.0; // 2^53
    }

    public float nextSingle() {
        return (nextUInt32() >> 8) / 16777216.0f; // 2^24
    }
}
