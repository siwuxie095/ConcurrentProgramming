package com.siwuxie095.concurrent.chapter6th.example2nd;

/**
 * @author Jiajing Li
 * @date 2020-09-28 07:20:10
 */
public class Main {

    /**
     * 函数式编程简介：函数是一等公民
     *
     * 在理解函数是一等公民这句话时，不妨先看一下一种非常常用的互联网语言 JavaScript。JavaScript 并不是严格
     * 意义上的函数式编程，不过它也不是属于严格的面向对象。但是如果愿意的话，既可以把它当作面向对象语言，也可以
     * 把它当作函数式语言。因此称之为多范式语言，可能更加合适。
     *
     * 如果使用 jQuery，可能经常会使用如下的代码：
     *
     * $("button").click(function() {
     *     $("li").each(function() {
     *         alert($(this).text())
     *     });
     * });
     *
     * 注意这里 each() 函数的参数是一个匿名函数，在遍历所有的 li 节点时，会弹出 li 节点的文本内容。将函数作为
     * 参数传递给另外一个函数，这是函数式编程的特性之一。
     *
     * 再来看另外一个示例：
     *
     * function f1() {
     *     var n = 1;
     *     function f2() {
     *         alert(n);
     *     }
     *     return f2;
     * }
     * var result = f1();
     * result();    // 1
     *
     * 这也是一段 JavaScript 代码，在这段代码中，注意函数 f1 的返回值，它返回了函数 f2。然后将返回的函数 f2
     * 赋值给了 result，实际上，此时的 result 就是一个函数，并且指向 f2。对 result 的调用，就会打印 n 的值。
     *
     * PS：jQuery 是一个 JavaScript 框架，是对 JavaScript 语言的封装。
     *
     * 函数可以作为另外一个函数的返回值，也是函数式编程的重要特点。
     *
     * 总结：
     * （1）函数可以作为另外一个函数的参数。
     * （2）函数可以作为另外一个函数的返回值。
     *
     *
     * 另："函数是一等公民" 有好几种说法，知道说的是同一个东西即可。
     * （1）函数是一等公民（推荐）
     * （2）函数是头等公民
     * （3）函数是第一等公民
     * （4）函数作为一等公民
     */
    public static void main(String[] args) {

    }

}
