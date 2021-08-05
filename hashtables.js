/**
 * Hash tables -> key value pair
 * basket.grapes = 100
 * key = 'grapes' -> done with hash function -> converts the key to a numbered value
 * value = 100
 */

/**
 * A hash function -> a function that generates a value of fixed length for each inputs that it gets
 */

class HashTable {
    constructor(size){
        this.data = new Array(size);
    }
    /**
     * Developer standard for having underscore -> meaning it should be private
     * more like a convention it doesn't really work
     * @param {} key 
     * @returns 
     */
    _hash(key){
        let hash  = 0;
        for (let i = 0;i<key.length;i++){
            hash = (hash + key.charCodeAt(i)*i) % this.data.length;
        }
        return hash;
    }
    setAsChaining(key,value){
        let index = this._hash(key);
        if (!this.data[index]){
            this.data[index] = [];
            this.data[index].push({key,value});
        } else {
            this.data[index].push({key,value})
        }
        
    }
    set(key,value){
        let assigned = false;
        let index = this._hash(key);
        if (!this.data[index]){
            this.data[index] = [key,value];
        } else {
            console.log("Collison: Iterating")
            for (let i = index ;i< this.data.length;i++){
                if (!this.data[i])
                   {
                    this.data[i] = [key,value];
                    assigned = true;    
                   } 
            }
            if (!assigned){
                for (let i = 0;i<index;i++) {
                    if (!this.data[i]) {
                     this.data[i] = [key,value];
                     assigned = true;    
                    } 
                }
                if (!assigned){
                    console.log("No space left");
                    return false;
                }
            }
        }
        return true;
    }
    getFromChaining(key){
        let index = this._hash(key);
        let chained = this.data[index];
        if (chained){
            for (let i = 0;i<chained.length;i++){
                if (chained[i].key === key){
                    return chained[i];
                }
            }
        }
        
        console.log("Not found");
        return false;
    }
    get (key){
        let index = this._hash(key);
        
        if (typeof this.data[index] != 'undefined' && this.data[index][0]===key){
            return this.data[index][1];
        }
        else{
            
            for (let i = index+1;i<this.data.length;i++){
                if ( typeof this.data[index] != 'undefined' && this.data[i][0]===key){
                    return this.data[i][1];
                }
            }
            for (let i = 0;i<index;i++){
                if (typeof this.data[index] != 'undefined' && this.data[i][0]===key){
                    return this.data[i][1];
                }
            }
        }
        console.log("Not found");
        return false;
    }
    keys(){
        const keysArray = [];
        for (let i = 0;i<this.data.length;i++){
            if (this.data[i]){
                for (let j=0 ; j<this.data[i].length;j++){
                    keysArray.push(this.data[i][j].key);
                }
              
            }
        }
        return keysArray
    }
}

const myHashTable = new HashTable(5);
myHashTable.setAsChaining('grapes',10);
myHashTable.setAsChaining('apples',100);
myHashTable.setAsChaining('bananas',1000);
myHashTable.setAsChaining('cherries',10000);
myHashTable.setAsChaining('luleshtrydhe',100000);
console.log(myHashTable.data)
console.log("Findng data with key 'luleshtrydhe' ");
console.log(myHashTable.getFromChaining("luleshtrydhe"));
console.log(myHashTable.keys())
//myHashTable.get('grapes');

/**
 * Google coding question
 * Given an array = [2,5,1,2,3,5,1,2,4]
 * find the first recurring element
 * (should return 2)
 */
 class HashTable_2 {
    constructor(size){
        this.data = new Array(size);
    }
    /**
     * Developer standard for having underscore -> meaning it should be private
     * more like a convention it doesn't really work
     * @param {} key 
     * @returns 
     */
    _hash(key){
        let hash  = 0;
        for (let i = 0;i<key.length;i++){
            hash = (hash + key.charCodeAt(i)*i) % this.data.length;
        }
        return hash;
    }
    setAsChaining(key,value){
        let index = this._hash(key);
        if (!this.data[index]){
            this.data[index] = [];
            this.data[index].push({key,value});
        } else {
            this.data[index].push({key,value})
        }
        
    }
    _modifiedHash(key){
        return key%(this.data.length)
    }
    firstRecurringElement(array){
        for (let i =0 ; i<array.length;i++){
            let index = this._modifiedHash(array[i]);
            console.log("Index for "+array[i] + " is "+index)
            if (!this.data[index]){
                this.data[index] = [];
                this.data[index].push({key:array[i],value:true});
            } else {
                return array[i];
            }
        }

    }
    getFromChaining(key){
        let index = this._hash(key);
        let chained = this.data[index];
        if (chained){
            for (let i = 0;i<chained.length;i++){
                if (chained[i].key === key){
                    return chained[i];
                }
            }
        }
        
        console.log("Not found");
        return false;
    }
    
    keys(){
        const keysArray = [];
        for (let i = 0;i<this.data.length;i++){
            if (this.data[i]){
                for (let j=0 ; j<this.data[i].length;j++){
                    keysArray.push(this.data[i][j].key);
                }
              
            }
        }
        return keysArray
    }
}
function firstRecurringElement(array){
    /**
     * 1) Loop through array and try to add each element into a hashmap
     * 2) When the first collision is encountered that is the first element that has found it's duplicate
      * this doesn't work cause 2 and 12 might have the same index in the data object
    */
    const map = new HashTable_2(array.length);
    console.log(map.firstRecurringElement(array));
    console.log(map.data)
}
/**
 * Remember -> maps in javascript don't need to be implemented
 * Simple objects in javascript are maps
 * @param {} input 
 * @returns 
 */
function firstRecurringElement2(input){
    //loop through the items add them to the hashtable and check if it exists
    let map = {};
    for (let i = 0;i<input.length;i++){
        if (map[input[i]]){
            return input[i];
        } else {
            map[input[i]] = true;
        }
    }
}
console.log("First recurring element is : ")
console.log(firstRecurringElement2([0,2,12,4,5,9,1,3,4,5]))