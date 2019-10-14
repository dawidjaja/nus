/**
 * MoveToFrontArrayList
 * Description: 
 * CS2040
 * 
 * You need to create a constructor and implement search.
 */


/**
 * 
 */
public class MoveToFrontArrayList extends ArrayList {

    MoveToFrontArrayList(int length){
        super(length);
    }

    public boolean search(int key){
        for (int i = 0; i <= m_max; i++) {
            if (m_list[i] == key) {
                for (int j = i - 1; j >= 0; j--) {
                    m_list[j] = m_list[j + 1] ^ m_list[j];
                    m_list[j + 1] = m_list[j + 1] ^ m_list[j];
                    m_list[j] = m_list[j + 1] ^ m_list[j];
                }
                return true;
            }
        }
        return false;
    }
    
}
