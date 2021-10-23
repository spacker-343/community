package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符
    private static final String REPLACEMENT = "*";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void innit() {
        // 初始化前缀树
        // 从类路径下获取文件，也就是target的目录
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败：" + e.getMessage());
        }
    }

    /**
     * 讲一个铭感次添加到前缀树
     */
    private void addKeyword(String keyword) {

        // 多叉树
        TrieNode tempNode = rootNode;
        for (char c : keyword.toCharArray()) {
            TrieNode subNode = tempNode.getSubNode(c);
            // 为空的话就代表这个字符还没有被添加
            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            // 指向子节点，进入下一层循环
            tempNode = subNode;
        }
        tempNode.setKeywordEnd(true);

    }

    /**
     * @param c
     * @return true表示是特殊符号
     */
    private boolean isSymbol(Character c) {
        // 后面为东亚文字范围
        // 前面判断是否为数字或字母
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * 过滤字符
     *
     * @param text
     * @return
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        TrieNode tempNode = rootNode;

        // 保存结果
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {

            // 保存过滤的一段字符
            StringBuilder filterS = new StringBuilder();

            // 敏感字符开始位置
            int sensitiveCur = i;
            // 注意不要让sensitiveCur越界
            while (sensitiveCur < text.length() && (tempNode.getSubNode(text.charAt(sensitiveCur)) != null || isSymbol(text.charAt(sensitiveCur)))) {
                if (isSymbol(text.charAt(sensitiveCur))) {
                    // 为特殊字符时，拼接上
                    filterS.append(text.charAt(sensitiveCur));
                    sensitiveCur++;
                    continue;
                }
                // 为敏感字符就拼接上*
                filterS.append("*");
                // 当前节点更新为它的子节点
                tempNode = tempNode.getSubNode(text.charAt(sensitiveCur));
                sensitiveCur++;
            }

            if (tempNode.isKeywordEnd) {
                // 是敏感字符串就拼接过滤后的filterS
                sb.append(filterS);
                // 那么[i, sensitiveCur)是敏感字符，更新i
                i = sensitiveCur - 1;
            } else {
                // 不是就拼接当前字符
                sb.append(text.charAt(i));
            }
            // 重置tempNode
            tempNode = rootNode;
        }
        return sb.toString();
        // ----3.1算法
//        if(StringUtils.isBlank(text)){
//            return null;
//        }
//        // 指针1
//        TrieNode tempNode = rootNode;
//        // 指针2
//        int begin = 0;
//        // 指针3
//        int position = 0;
//        // 结果
//        StringBuilder sb = new StringBuilder();
//
//        while(begin < text.length()){
//            if(position < text.length()) {
//                Character c = text.charAt(position);
//
//                // 跳过符号
//                if (isSymbol(c)) {
//                    if (tempNode == rootNode) {
//                        begin++;
//                        sb.append(c);
//                    }
//                    position++;
//                    continue;
//                }
//
//                // 检查下级节点
//                tempNode = tempNode.getSubNode(c);
//                if (tempNode == null) {
//                    // 以begin开头的字符串不是敏感词
//                    sb.append(text.charAt(begin));
//                    // 进入下一个位置
//                    position = ++begin;
//                    // 重新指向根节点
//                    tempNode = rootNode;
//                }
//                // 发现敏感词
//                else if (tempNode.isKeywordEnd()) {
//                    sb.append(REPLACEMENT);
//                    tempNode=rootNode;
//                    begin = ++position;
//                }
//                // 检查下一个字符
//                else {
//                    position++;
//                }
//            }
//            // position遍历越界仍未匹配到敏感词
//            else{
//                sb.append(text.charAt(begin));
//                position = ++begin;
//                tempNode = rootNode;
//            }
//        }
//        return sb.toString();
    }

    /**
     * 前缀树
     */
    private class TrieNode {

        // 关键词结束标识
        private boolean isKeywordEnd = false;

        // 子节点(key是下级字符,value是下级节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }


    }

}
