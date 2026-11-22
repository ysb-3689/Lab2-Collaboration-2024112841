/**
 * 给定一个无序数组 nums，返回数组排序后相邻元素之间的最大差值。
 * 如果数组元素个数小于 2，则返回 0。
 *
 * <p>实现方式：LSD（最低位优先）基数排序后扫描相邻差值。</p>
 *
 * <p>复杂度：设 d 为最大值的十进制位数（nums[i] &lt;= 10^9 时 d &lt;= 10），
 * 时间复杂度为 O(d * n)，d 为常数时即 O(n)；额外空间复杂度为 O(n)。</p>
 *
 * <p>注意：本方法会就地对 nums 排序，调用方如果后续还要使用原数组，需自行传入副本。</p>
 */
class Solution4 {

    /**
     * 计算排序后相邻元素的最大差值。
     *
     * @param nums 非负整数数组（元素取值范围 0 ~ 2^31-1），调用后会被升序排序
     * @return 相邻元素的最大差值；元素个数小于 2 时返回 0
     * @throws IllegalArgumentException nums 为 null，或包含负数元素时抛出
     *                                  （基数排序按十进制位统计，无法处理负数）
     */
    public int maximumGap(int[] nums) {
        // 【评审补充】显式的入参校验：原实现遇到 null 抛 NullPointerException、
        // 遇到负数抛 "Index -5 out of bounds for length 10"，异常信息无法说明问题所在
        if (nums == null) {
            throw new IllegalArgumentException("nums 不能为 null");
        }

        int n = nums.length;

        if (n < 2) {
            return 0;
        }

        // 【评审补充】基数排序的每一位都要对 10 取模，负数的余数仍为负数，
        // 会直接造成数组下标越界，因此在这里拒绝负数输入并给出明确原因
        int maxVal = nums[0];
        for (int value : nums) {
            if (value < 0) {
                throw new IllegalArgumentException("nums 不能包含负数：" + value);
            }
            if (value > maxVal) {
                maxVal = value;
            }
        }

        int[] buf = new int[n];

        // 【评审修改】原文在「review: 简化 exp 类型」中把 long exp 改成 int exp。
        // 这一步并不安全：当最大值超过 10^9（例如 Integer.MAX_VALUE）时，
        // exp *= 10 会在 int 上溢出成 1410065408、1215752192，最后变成负数，
        // 于是 (value / exp) % 10 得到负数下标，抛出 ArrayIndexOutOfBoundsException。
        // 用 long 保存位权，成本可以忽略，却可以彻底避免溢出。
        long exp = 1L;

        while (maxVal >= exp) {
            int[] count = new int[10];

            // 统计当前位上每个数字出现的次数
            for (int value : nums) {
                int digit = (int) ((value / exp) % 10);
                count[digit]++;
            }

            // 计算前缀和，确定每个数字在结果数组中的结束位置
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }

            // 从后向前遍历，保证计数排序的稳定性
            for (int i = n - 1; i >= 0; i--) {
                int digit = (int) ((nums[i] / exp) % 10);
                buf[--count[digit]] = nums[i];
            }

            // 将本轮排序结果复制回原数组
            System.arraycopy(buf, 0, nums, 0, n);

            // 处理下一位
            exp *= 10;
        }

        int maxGap = 0;

        // 数组已经完成排序，计算相邻元素的最大差值
        for (int i = 1; i < n; i++) {
            maxGap = Math.max(maxGap, nums[i] - nums[i - 1]);
        }

        return maxGap;
    }
}
