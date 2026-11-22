import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 测试用例总体设计原则：
 *
 * 1. 使用等价类划分，覆盖普通数组、单元素数组、重复元素数组。
 * 2. 使用边界值分析，覆盖最小数组长度、0 和 10^9。
 * 3. 使用多位十进制数据检查基数排序的各个位次。
 * 4. 使用固定随机种子的随机测试，并以 Arrays.sort 的结果作为测试预言。
 */
public class L2024112841_4_Test {

    private final Solution4 solution = new Solution4();

    /**
     * 测试目的：验证题目给出的普通示例。
     * 测试用例：[3, 6, 9, 1]，期望最大间隔为 3。
     */
    @Test
    void testExample() {
        assertEquals(
                3,
                solution.maximumGap(new int[]{3, 6, 9, 1})
        );
    }

    /**
     * 测试目的：验证数组元素少于两个时返回 0。
     * 测试用例：[10]，期望结果为 0。
     */
    @Test
    void testSingleElement() {
        assertEquals(
                0,
                solution.maximumGap(new int[]{10})
        );
    }

    /**
     * 测试目的：验证最小有效数组长度和题目允许的最大元素值。
     * 测试用例：[1, 1000000000]，期望结果为 999999999。
     */
    @Test
    void testTwoElementsAndUpperBoundary() {
        assertEquals(
                999999999,
                solution.maximumGap(new int[]{1, 1000000000})
        );
    }

    /**
     * 测试目的：验证全部元素相同时最大间隔为 0。
     * 测试用例：[1, 1, 1]，期望结果为 0。
     */
    @Test
    void testDuplicateElements() {
        assertEquals(
                0,
                solution.maximumGap(new int[]{1, 1, 1})
        );
    }

    /**
     * 测试目的：验证包含 0 以及多个十进制位的数据。
     * 测试用例：[0, 10, 100, 1000]，期望最大间隔为 900。
     */
    @Test
    void testMultipleDecimalPositions() {
        assertEquals(
                900,
                solution.maximumGap(new int[]{0, 10, 100, 1000})
        );
    }

    /**
     * 测试目的：使用大量确定性随机数据检查一般情况。
     * 测试用例：固定种子生成 200 个数组，并与排序后的结果对照。
     */
    @Test
    void testRandomArraysAgainstSortingOracle() {
        Random random = new Random(20261115L);

        for (int round = 0; round < 200; round++) {
            int length = 1 + random.nextInt(50);
            int[] nums = new int[length];

            for (int i = 0; i < length; i++) {
                nums[i] = random.nextInt(1_000_000_001);
            }

            int[] original = nums.clone();
            int[] sorted = nums.clone();
            Arrays.sort(sorted);

            int expected = 0;
            for (int i = 1; i < sorted.length; i++) {
                expected = Math.max(expected, sorted[i] - sorted[i - 1]);
            }

            assertEquals(
                    expected,
                    solution.maximumGap(nums),
                    "测试输入：" + Arrays.toString(original)
            );
        }
    }

    // ------------------------------------------------------------------
    // 以下用例由代码评审者补充（2024112758）
    // ------------------------------------------------------------------

    /**
     * 测试目的：验证空数组（长度为 0）也能安全返回 0。
     * 测试用例：[]，期望结果为 0。
     */
    @Test
    void testEmptyArray() {
        assertEquals(0, solution.maximumGap(new int[]{}));
    }

    /**
     * 测试目的：回归测试「位权 exp 使用 int 会溢出」的缺陷。
     * 测试用例：[0, Integer.MAX_VALUE]。修复前 exp 依次变成 1410065408、1215752192、
     *          最后溢出为负数，导致 (value / exp) % 10 为负下标，抛
     *          ArrayIndexOutOfBoundsException: Index -2 out of bounds for length 10。
     * 预期结果：返回 2147483647。
     */
    @Test
    void testIntMaxValueBoundary() {
        assertEquals(
                Integer.MAX_VALUE,
                solution.maximumGap(new int[]{0, Integer.MAX_VALUE})
        );
    }

    /**
     * 测试目的：验证 null 输入被转换为语义明确的 IllegalArgumentException。
     * 测试用例：nums = null。
     * 预期结果：抛出 IllegalArgumentException（而不是 NullPointerException）。
     */
    @Test
    void testNullInputRejected() {
        assertThrows(IllegalArgumentException.class, () -> solution.maximumGap(null));
    }

    /**
     * 测试目的：验证负数输入被明确拒绝，而不是抛出难以理解的下标越界异常。
     * 测试用例：[-5, 3]。基数排序按十进制位统计，负数的余数为负，无法处理。
     * 预期结果：抛出 IllegalArgumentException。
     */
    @Test
    void testNegativeInputRejected() {
        assertThrows(IllegalArgumentException.class, () -> solution.maximumGap(new int[]{-5, 3}));
    }

    /**
     * 测试目的：验证大规模输入（题目上限 10^5）下结果正确且耗时可控（O(dn) 基数排序）。
     * 测试用例：固定种子生成的 100000 个 [0, 10^9] 随机整数，以 Arrays.sort 的结果为预言。
     * 预期结果：与排序预言一致。
     */
    @Test
    void testLargeScaleAgainstSortingOracle() {
        Random random = new Random(2024112758L);
        int length = 100_000;
        int[] nums = new int[length];
        for (int i = 0; i < length; i++) {
            nums[i] = random.nextInt(1_000_000_001);
        }

        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int expected = 0;
        for (int i = 1; i < sorted.length; i++) {
            expected = Math.max(expected, sorted[i] - sorted[i - 1]);
        }

        assertEquals(expected, solution.maximumGap(nums));
    }
}