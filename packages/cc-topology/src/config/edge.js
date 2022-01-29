/**
 * @author: winyuan
 * @data: 2019/08/16
 * @repository: https://github.com/winyuan
 * @description: 线条的后期设置
 */
//配置是否有箭头
import G6 from '@antv/g6'
export default {
  type: 'solidline',
  style: {
    startArrow: false,
    endArrow: {
      path: G6.Arrow.vee(10, 20, 15),
      d: 25
    }
  }
}
