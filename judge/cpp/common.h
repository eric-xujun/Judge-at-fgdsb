#pragma once

#include <iostream>
#include <algorithm>
#include <utility>
#include <stack>
#include <deque>
#include <list>
#include <set>
#include <map>
#include <unordered_set>
#include <unordered_map>
#include <assert.h>
#include <strstream>
#include <queue>
#include <numeric>
#include <iomanip>
#include <fstream>
#include <unistd.h>
#include <chrono>

using namespace std;

struct TreeNode {
    TreeNode(int v = 0) :val(v){}
    int val{ 0 };
    TreeNode* left{ nullptr };
    TreeNode* right{ nullptr };
};

struct TreeNodeWithParent {
    TreeNodeWithParent(int v = 0) :val(v){}
    int val{ 0 };
    TreeNodeWithParent* left{ nullptr };
    TreeNodeWithParent* right{ nullptr };
    TreeNodeWithParent* parent{ nullptr };
};

struct ListNode {
    ListNode(int v = 0) :val(v){}
    int val{ 0 };
    ListNode* next{ nullptr };
};

struct DoublyListNode {
    DoublyListNode(int v = 0) :val(v){}
    int val{ 0 };
    DoublyListNode* next{ nullptr };
    DoublyListNode* prev{ nullptr };
};

struct UndirectedGraphNode {
    int label;
    vector<UndirectedGraphNode *> neighbors;
    UndirectedGraphNode(int x) : label(x) {};
};

////////////////////////////////////////////////////////////////////////

class Iterator {
public:
    Iterator(vector<int>& data) : _data(data) {}
    int get_next() { return _data[_pointer++]; }
    bool has_next() { return _pointer < _data.size(); }
    
private:
    vector<int> _data;
    size_t _pointer{ 0 };
};

////////////////////////////////////////////////////////////////////////

struct Point {
    Point(int _x = 0, int _y = 0): x(_x), y(_y){}
    int x, y;
};

namespace std {
    template <>
    class hash<Point>{
        public :
        size_t operator()(const Point &c ) const {
            return std::hash<int>()(c.x * 100000 + c.y);
        }
    };
};

bool operator== (Point &i1, Point &i2){
    return i1.x == i2.x && i1.y == i2.y;
}

bool operator== (const Point &i1, const Point &i2){
    return i1.x == i2.x && i1.y == i2.y;
}

bool operator< (Point &__x, Point &__y){
    return __x.x < __y.x || (!(__y.x < __x.x) && __x.y < __y.y);
}

bool operator< (const Point &__x, const Point &__y){
    return __x.x < __y.x || (!(__y.x < __x.x) && __x.y < __y.y);
}

////////////////////////////////////////////////////////////////////////

struct Interval {
    Interval(int b = 0, int e = 0): begin(b), end(e){}
    int begin, end;
};

bool operator== (Interval &i1, Interval &i2){
    return i1.begin == i2.begin && i1.end == i2.end;
}

bool operator== (const Interval &i1, const Interval &i2){
    return i1.begin == i2.begin && i1.end == i2.end;
}

namespace std {
    template <>
    class hash<Interval>{
        public :
        size_t operator()(const Interval &c ) const {
            return std::hash<int>()(c.begin * 100000 + c.end);
        }
    };
};

////////////////////////////////////////////////////////////////////////

TreeNode* clone(TreeNode*& nd) {
    if(!nd) return nullptr;
    TreeNode* r = new TreeNode(nd->val);
    r->left = clone(nd->left);
    r->right = clone(nd->right);
    return r;
}

TreeNodeWithParent* clone(TreeNodeWithParent*& nd) {
    if(!nd) return nullptr;
    TreeNodeWithParent* r = new TreeNodeWithParent(nd->val);
    r->left = clone(nd->left);
    if(r->left) r->left->parent = r;
    r->right = clone(nd->right);
    if(r->right) r->right->parent = r;
    return r;
}

template<typename T>
vector<T*> clone(vector<T*>& nd) {
    vector<T*> r;
    for(auto n : nd) {
        r.push_back(clone(n));
    }
    return r;
}

template<typename T>
T clone(T& n) {
    T ret = n;
    return ret;
}

bool equals(TreeNode* n1, TreeNode* n2) {
    if(!n1 && !n2) return true;
    if(!n1 || !n2) return false;
    return n1->val == n2->val;
}

bool equals(TreeNodeWithParent* n1, TreeNodeWithParent* n2) {
    if(!n1 && !n2) return true;
    if(!n1 || !n2) return false;
    return n1->val == n2->val;
}

template<typename T>
string node_to_string(T* n) {
    return !n ? "nullptr" : to_string(n->val);
}

////////////////////////////////////////////////////////////////////////

template<typename T>
ostream& operator<< (ostream& out, const vector<T>& v) {
    out << "[";
    size_t last = v.size() - 1;
    for(size_t i = 0; i < v.size(); ++i) {
        out << v[i];
        if (i != last) out << ", ";
    }
    out << "]";
    return out;
}

ostream& operator<< (ostream& out, const Interval& v) {
    out << "[" << v.begin << ", " << v.end << "]";
    return out;
}

ostream& operator<< (ostream& out, const Point& v) {
    out << "(" << v.x << ", " << v.y << ")";
    return out;
}

void serialize(const TreeNode *root, ostream& out) {
    if (!root) {
        out << "# ";
        return;
    }
    out << root->val << " ";
    serialize(root->left, out);
    serialize(root->right, out);
}

ostream& operator<< (ostream& out, const TreeNode* v) {
    serialize(v, out);
    return out;
}

bool test_wiggle(vector<int>& arr) {
    if(arr.empty()) return true;
    bool test_flag = true;
    for(size_t i = 0; i < arr.size()-1; ++i) {
        if(test_flag) {
            if(arr[i] > arr[i+1]) return false;
        } else {
            if(arr[i] < arr[i+1]) return false;
        }
        test_flag = !test_flag;
    }
    return true;
}

bool test_anagram(vector<int>& a0, vector<int>& a1) {
    if(a0.size() != a1.size()) return false;
    vector<int> t0 = a0;
    vector<int> t1 = a1;
    sort(t0.begin(), t0.end());
    sort(t1.begin(), t1.end());
    return t0 == t1;
}

////////////////////////////////////////////////////////////////////////

bool same_tree(TreeNode *p, TreeNode *q) {
    if(!p && !q) return true;
    if(!p || !q) return false;
    if(p->val != q->val) return false;
    return same_tree(p->left,q->left) && same_tree(p->right,q->right);
}

TreeNode* read_tree(ifstream& in, int nums) {
    if(nums == 0) {
        return nullptr;
    }
    string cur;
    in >> cur;
    
    if(cur == "#") {
        return nullptr;
    }
    auto root = new TreeNode(atoi(cur.c_str()));
    nums--;
    root->left = read_tree(in, nums);
    nums--;
    root->right = read_tree(in, nums);
    return root;
}

TreeNodeWithParent* read_tree_with_parent(ifstream& in, int nums) {
    if(nums == 0) {
        return nullptr;
    }
    string cur;
    in >> cur;
    
    if(cur == "#") {
        return nullptr;
    }
    auto root = new TreeNodeWithParent(atoi(cur.c_str()));
    nums--;
    root->left = read_tree_with_parent(in, nums);
    if(root->left) root->left->parent = root;
    nums--;
    root->right = read_tree_with_parent(in, nums);
    if(root->right) root->right->parent = root;
    return root;
}

template <typename T>
vector<T> read_array(ifstream& in) {
    vector<T> ret;
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        T n;
        in >> n;
        ret.push_back(n);
    }
    return ret;
}

template <typename T>
void read_array(ifstream& in, vector<T>& ret) {
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        T n;
        in >> n;
        ret.push_back(n);
    }
}

void read_array(ifstream& in, vector<Interval>& ret) {
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        Interval n;
        in >> n.begin;
        in >> n.end;
        ret.push_back(n);
    }
}

void read_array(ifstream& in, vector<Point>& ret) {
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        Point n;
        in >> n.x;
        in >> n.y;
        ret.push_back(n);
    }
}

void read_array(ifstream& in, vector<TreeNode*>& ret) {
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        int n = 0;
        in >> n;
        ret.push_back(read_tree(in, n));
    }
}

void read_array(ifstream& in, vector<TreeNodeWithParent*>& ret) {
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        int n = 0;
        in >> n;
        ret.push_back(read_tree_with_parent(in, n));
    }
}

template <typename T>
vector<vector<T>> read_matrix(ifstream& in) {
    vector<vector<T>> ret;
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        ret.push_back(vector<T>());
        read_array<T>(in, ret.back());
    }
    return ret;
}

template <typename T>
void read_matrix(ifstream& in, vector<vector<T>>& ret) {
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        ret.push_back(vector<T>());
        read_array(in, ret.back());
    }
}

void read_matrix(ifstream& in, vector<vector<Interval>>& ret) {
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        ret.push_back(vector<Interval>());
        read_array(in, ret.back());
    }
}

template <typename T>
void read_matrix_arr(ifstream& in, vector<vector<vector<T>>>& ret) {
    int nums = 0;
    in >> nums;
    ret.reserve(nums);
    for(int i = 0; i < nums; ++i) {
        ret.push_back(vector<vector<T>>());
        read_matrix(in, ret.back());
    }
}

////////////////////////////////////////////////////////////////////////

int g_old_stdout = 0;

void capture_stdout() {
    g_old_stdout = dup(1);
    freopen("judge/stdout.txt", "w", stdout);
}

void release_stdout() {    
    fclose(stdout);
    *stdout = *fdopen(g_old_stdout, "w");
}

