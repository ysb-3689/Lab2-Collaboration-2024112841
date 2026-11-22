# OSSDP-Lab2

哈尔滨工业大学《开源软件开发实践》Lab 2 —— 实验内容 2 协作仓库（学号 2024112841）

本仓库由实验内容 1 的题目仓库复制而来，包含 20 道编程题目的实现；其中**第 4 题
（`Solution4.java`，排序后相邻元素的最大差值）**是本仓库负责修复的题目，配套单元测试为
`L2024112841_4_Test.java`。

## 第 4 题实现说明

`Solution4.maximumGap(int[] nums)`：先用 LSD 基数排序把数组升序排列，再扫描相邻差值取最大值；
元素个数小于 2 时返回 0。

- 时间复杂度：O(d·n)，d 为最大值的十进制位数（`nums[i] <= 10^9` 时 d ≤ 10），d 为常数时即 O(n)
- 额外空间复杂度：O(n)
- 注意：方法会**就地排序** `nums`，调用方若还需要原数组请自行传入副本

## 如何运行单元测试

```bash
# 1. 获取 JUnit 5 控制台启动器（也可以直接在 IDE 中使用 JUnit 5）
curl -L -o junit-platform-console-standalone-1.11.4.jar \
  https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar

# 2. 编译（只需要第 4 题与被测类；其余 Solution 文件中有多个同名 Solution 类，不能一起编译）
javac -encoding UTF-8 -d out -cp junit-platform-console-standalone-1.11.4.jar \
  Solution4.java L2024112841_4_Test.java

# 3. 执行
java -jar junit-platform-console-standalone-1.11.4.jar execute \
  --class-path out --select-class L2024112841_4_Test --details=tree
```

## 评审记录

代码评审者（2024112758）在本仓库上提出的改进见 Pull Request 描述，主要包括：

1. `exp` 位权由 `int` 改回 `long`，避免最大值超过 10^9 时 `exp *= 10` 溢出成负数、进而数组下标越界；
2. 补充 `null` 与负数入参的显式校验，抛出语义明确的 `IllegalArgumentException`；
3. 用一趟普通循环替代 `Arrays.stream(nums).max()`，去掉 Stream/Optional 开销；
4. 补充 5 个测试用例（空数组、`Integer.MAX_VALUE` 边界、null、负数、10^5 规模）。
