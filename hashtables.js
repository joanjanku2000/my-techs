// a must for interviews
// js hash object as hashtables
// python has dictionaries

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
}

const myHashTable = new HashTable(100);
myHashTable.set('grapes',10);
myHashTable.set('grapeselsrs',100);
myHashTable.set('gsdf',1000);
myHashTable.set('grapesedsdsflsrs',10000);
myHashTable.set('grerwfeapeselsrs',100000);
console.log(myHashTable.data)
console.log("Findng data with key 'grapes' ");
console.log(myHashTable.get("grapes"));
//myHashTable.get('grapes');