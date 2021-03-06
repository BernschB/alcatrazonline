/**
 * FH Technikum-Wien,
 * BICSS - Sommersemester 2011
 *
 * Softwarearchitekturen und Middlewaretechnologien
 * Alcatraz - Remote - Projekt
 * Gruppe B2
 *
 *
 * @author Christian Fossati
 * @author Stefan Schramek
 * @author Michael Strobl
 * @author Sebastian Vogel
 * @author Juergen Zornig
 *
 *
 * @date 2011/03/10
 *
 **/

package at.technikum.sam.remote.alcatraz.commons;

import java.io.Serializable;

/**
 *
 * PlayerAdapter associates a client stub with its players name
 */
public class PlayerAdapter implements Serializable{

    private String name;
    private IClient clientstub;

    public PlayerAdapter(String name, IClient clientstub) {
        this.name = name;
        this.clientstub = clientstub;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IClient getClientstub() {
        return this.clientstub;
    }

    public void setClientstub(IClient clientstub) {
        this.clientstub = clientstub;
    }

    @Override
    public boolean equals(Object o) {

        if(!o.getClass().equals(this.getClass())){
            return false;
        }
        if(o.hashCode() != this.hashCode()) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
    
}
