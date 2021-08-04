// GIven 2 arrays create a function that let's the user know (true/false)
// whether these 2 arrays contain any common items

/**
 * const array1 = ['a','b','c','x','y'];
 * const array2 = ['c','e','f','a'];
 */
 const array1 = ['a','b','c','x','y'];
 const array2 = ['d','e','b','g'];

 /**
  * O(n^2) -> time
  * O(1) -> space complexity since we're using existing arrays and no other ds
  * @param {} arr1 
  * @param {*} arr2 
  * @returns 
  */
function containCommonItems(arr1,arr2){
    let l1 = arr1.length;
    let l2 = arr2.length;

    for (let i = 0;i<l1;i++){
        for (let j = 0;j<l2;j++){
            if (arr1[i]==arr2[j]){
                return true;
            }
        }
    }
    return false;

}
//another solution
/**
 * convert the first array to an object so we can loop through the second array only
 * array1 ==> obj {
 *  a: true
 *  b:true
 *  c:true
 *  x:true
 * }
 * array2[index] == obj.properties
 */

function containsCommonItems(arr1,arr2){
    /**
     * loop through first array and create an object where properties === items in the array
     * loop through second array and check if item in second array exists on created object
     * better solution -> we don't have nested for loops
     * O(n1+n2) better than O(n1*n2) -> time
     * O(n1) -> space complexity -> because we have another data structure to keep one array
     */
    let map = {}; // just like a hashmap
    for (let i = 0;i<arr1.length;i++){
        if (!map[i]){ // doesn;t exist
            const item = arr1[i];
            map[item] = true; // map[a] = true
        }
    }
    console.log(map);
    if (arr2!=null){

    for (let i = 0;i<arr2.length;i++){
        if (map[arr2[i]]){
            return true;
        }
    }
    } else{
        console.log("Please give a second array")
    }
    return false;
}
console.log(containsCommonItems(array1,null));