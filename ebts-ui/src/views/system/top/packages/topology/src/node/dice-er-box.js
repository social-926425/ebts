/**
 * @author: clay
 * @data: 2021/5/15 0:16
 * @email: clay@hchyun.com
 * @description: node
 */
import utils from '../utils/index'

const itemHeight = 30;
export default {
    name: 'dice-er-box',
    options: {
        setState(name, value, item,group) {
            // 设置节点状态
            // utils.node.setState(name, value, item,group);
            // 设置锚点状态
            utils.anchor.setState(name, value, item);
            // //设置收缩状态
            // utils.collapse.setState(item)
        },
        draw(cfg, group) {
            const width = 250;
            const height = 316;
            const itemCount = 10;
            const boxStyle = {
                stroke: "#096DD9",
                radius: 4,
            };

            const {
                attrs = [],
                startIndex = 0,
                selectedIndex,
                collapsed,
                icon,
            } = cfg;
            const list = attrs;
            const afterList = list.slice(
                Math.floor(startIndex),
                Math.floor(startIndex + itemCount - 1)
            );
            const offsetY = (0.5 - (startIndex % 1)) * itemHeight + 30;

            group.addShape("rect", {
                attrs: {
                    fill: boxStyle.stroke,
                    height: 30,
                    width,
                    radius: [boxStyle.radius, boxStyle.radius, 0, 0],
                },
                draggable: true,
            });

            let fontLeft = 12;

            if (icon && icon.show !== false) {
                group.addShape("image", {
                    attrs: {
                        x: 8,
                        y: 8,
                        height: 16,
                        width: 16,
                        ...icon,
                    },
                });
                fontLeft += 18;
            }

            group.addShape("text", {
                attrs: {
                    y: 22,
                    x: fontLeft,
                    fill: "#fff",
                    text: cfg.label,
                    fontSize: 12,
                    fontWeight: 500,
                },
            });

            group.addShape("rect", {
                attrs: {
                    x: 0,
                    y: collapsed ? 30 : 300,
                    height: 15,
                    width,
                    fill: "#eee",
                    radius: [0, 0, boxStyle.radius, boxStyle.radius],
                    cursor: "pointer",
                },
                name: collapsed ? "expand" : "collapse",
            });

            group.addShape("text", {
                attrs: {
                    x: width / 2 - 6,
                    y: (collapsed ? 30 : 300) + 12,
                    text: collapsed ? "+" : "-",
                    width,
                    fill: "#000",
                    radius: [0, 0, boxStyle.radius, boxStyle.radius],
                    cursor: "pointer",
                },
                name: collapsed ? "expand" : "collapse",
            });

            const keyshape = group.addShape("rect", {
                attrs: {
                    x: 0,
                    y: 0,
                    width,
                    height: collapsed ? 45 : height,
                    ...boxStyle,
                },
                draggable: true,
            });

            if (collapsed) {
                return keyshape;
            }

            const listContainer = group.addGroup({});
            listContainer.setClip({
                type: "rect",
                attrs: {
                    x: -8,
                    y: 30,
                    width: width + 16,
                    height: 300 - 30,
                },
            });
            listContainer.addShape({
                type: "rect",
                attrs: {
                    x: 1,
                    y: 30,
                    width: width - 2,
                    height: 300 - 30,
                    fill: "#fff",
                },
                draggable: true,
            });

            if (list.length > itemCount) {
                const barStyle = {
                    width: 4,
                    padding: 0,
                    boxStyle: {
                        stroke: "#00000022",
                    },
                    innerStyle: {
                        fill: "#00000022",
                    },
                };

                listContainer.addShape("rect", {
                    attrs: {
                        y: 30,
                        x: width - barStyle.padding - barStyle.width,
                        width: barStyle.width,
                        height: height - 30,
                        ...barStyle.boxStyle,
                    },
                });

                const indexHeight =
                    afterList.length > itemCount ?
                        (afterList.length / list.length) * height :
                        10;

                listContainer.addShape("rect", {
                    attrs: {
                        y: 30 +
                            barStyle.padding +
                            (startIndex / list.length) * (height - 30),
                        x: width - barStyle.padding - barStyle.width,
                        width: barStyle.width,
                        height: Math.min(height, indexHeight),
                        ...barStyle.innerStyle,
                    },
                });
            }
            if (afterList) {
                afterList.forEach((e, i) => {
                    const isSelected =
                        Math.floor(startIndex) + i === Number(selectedIndex);
                    let {
                        key = "", type
                    } = e;
                    if (type) {
                        key += " - " + type;
                    }
                    const label = key.length > 26 ? key.slice(0, 24) + "..." : key;

                    listContainer.addShape("rect", {
                        attrs: {
                            x: 1,
                            y: i * itemHeight - itemHeight / 2 + offsetY,
                            width: width - 4,
                            height: itemHeight,
                            radius: 2,
                            lineWidth: 1,
                            cursor: "pointer",
                        },
                        name: `item-${Math.floor(startIndex) + i}-content`,
                        draggable: true,
                    });

                    if (!cfg.hideDot) {
                        // utils.anchor.erDrawLeft(group,label,0,i * itemHeight + offsetY)
                        // utils.anchor.erDrawLeft(group,label,width,i * itemHeight + offsetY)
                        listContainer.addShape("marker", {
                            attrs: {
                                x: 0,
                                y: i * itemHeight + offsetY,
                                r: 3,
                                stroke: boxStyle.stroke,
                                fill: "white",
                                radius: 2,
                                lineWidth: 1,
                                cursor: "pointer",
                            },
                            name: 'marker-shape'
                        });
                        listContainer.addShape("marker", {
                            attrs: {
                                x: width,
                                y: i * itemHeight + offsetY,
                                r: 3,
                                stroke: boxStyle.stroke,
                                fill: "white",
                                radius: 2,
                                lineWidth: 1,
                                cursor: "pointer",
                            },
                            name: 'markerBg-shape'
                        });








                    }

                    listContainer.addShape("text", {
                        attrs: {
                            x: 12,
                            y: i * itemHeight + offsetY + 6,
                            text: label,
                            fontSize: 12,
                            fill: "#000",
                            fontFamily: "Avenir,-apple-system,BlinkMacSystemFont,Segoe UI,PingFang SC,Hiragino Sans GB,Microsoft YaHei,Helvetica Neue,Helvetica,Arial,sans-serif,Apple Color Emoji,Segoe UI Emoji,Segoe UI Symbol",
                            full: e,
                            fontWeight: isSelected ? 500 : 100,
                            cursor: "pointer",
                        },
                        name: `item-${Math.floor(startIndex) + i}`,
                    });
                });
            }


            // console.log(keyshape);
            return keyshape;
        },
        getAnchorPoints() {
            return [
                [0, 0],
                [1, 0],
            ];
        },
    }
}





