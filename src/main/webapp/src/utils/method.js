// 保留 2 位小数
export const toDecimal = (num) => {
    const x = parseFloat(num);
    return Math.round(x * 100) / 100;
};
