/**

@author Petr Jindráček
*/
package cz.vse.togather.utils;

import java.beans.PropertyEditorSupport;

import cz.vse.togather.domain.CategoryEnum;

public class CategoryPropertyEditor extends PropertyEditorSupport {
    
    public CategoryPropertyEditor() {
        
    }
    
    @Override
    public void setAsText(String text) {
        if (text == null || text.isEmpty()) {
            setValue(null);
        } else {
            setValue(CategoryEnum.getInstance(Integer.parseInt(text)));
        }
    }
    
    @Override
    public String getAsText() throws java.lang.IllegalArgumentException {
        CategoryEnum value = (CategoryEnum) getValue();
        return value.getName();
    }
}
