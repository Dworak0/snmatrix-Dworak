import React, { useState, useEffect } from 'react';
import './index.css';

const API_URL = 'http://localhost:8080/api/users';

function App() {
    const [view, setView] = useState('list');
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [formData, setFormData] = useState({
        email: '', lastName: '', firstName: '', phone: '',
        employeeType: 'Contractor', companyName: '', active: true
    });
    const [phoneError, setPhoneError] = useState('');

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = () => {
        fetch(API_URL)
            .then(res => res.json())
            .then(data => setUsers(data))
            .catch(err => console.error("Could not fetch users:", err));
    };

    const handleEdit = (user) => {
        setFormData(user);
        setSelectedUser(user);
        setView('edit');
    };

    const handleDelete = (id) => {
        fetch(`${API_URL}/${id}`, { method: 'DELETE' })
            .then(() => fetchUsers());
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const method = view === 'edit' ? 'PUT' : 'POST';
        const url = view === 'edit' ? `${API_URL}/${selectedUser.id}` : API_URL;

        fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        })
        .then(response => {
            if (response.ok) {
                setView('list');
                fetchUsers();
                setSelectedUser(null);
                setFormData({ email: '', lastName: '', firstName: '', phone: '', employeeType: 'Contractor', companyName: '', active: true });
            } else {
                alert("Could not save user. Check the server console for errors.");
            }
        });
    };

    return (
        <div className="main-wrapper">
            <nav className="navbar">
                <a href="#" onClick={() => setView('list')} className="navbar-brand">CoreMvcEvaluation</a>
                <a href="#" onClick={() => setView('list')} className="nav-link">Home</a>
                <a href="#" onClick={() => setView('list')} className="nav-link">Users</a>
                <a href="#" className="nav-link">Employee Types</a>
            </nav>

            <div className="container">
                {view === 'list' ? (
                    <div>
                        <h2>User List</h2>
                        <a href="#" onClick={() => { setView('create'); setFormData({email:'', lastName:'', firstName:'', phone:'', employeeType:'Contractor', companyName:'', active:true}); }} className="action-link">Create New</a>
                        
                        <table className="table">
                            <thead>
                                <tr>
                                    <th>Email Address</th>
                                    <th>Diplay Name</th>
                                    <th>Company Name</th>
                                    <th>User is Active</th>
                                    <th>Last Login</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {users.map(u => (
                                    <tr key={u.id}>
                                        <td>{u.email}</td>
                                        <td>{u.lastName}, {u.firstName}</td>
                                        <td>{u.companyName}</td>
                                        <td><input type="checkbox" checked={u.active} disabled /></td>
                                        <td>{u.lastLogin || ''}</td>
                                        <td style={{textAlign: 'right'}}>
                                            <a href="#" onClick={() => handleEdit(u)} className="action-link">Edit</a> | 
                                            <a href="#" onClick={() => { setSelectedUser(u); }} className="action-link"> Details</a> | 
                                            <a href="#" onClick={() => handleDelete(u.id)} className="action-link"> Delete</a>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                ) : (
                    <div>
                        <h2>{view === 'edit' ? 'Edit User' : 'Create User'}</h2>
                        <form onSubmit={handleSubmit} style={{marginTop: '20px'}}>
                            <div className="form-group"><label>Email Address</label><input type="email" required className="form-control" value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} /></div>
                            <div className="form-group"><label>Last Name</label><input type="text" required className="form-control" value={formData.lastName} onChange={e => setFormData({...formData, lastName: e.target.value})} /></div>
                            <div className="form-group"><label>First Name</label><input type="text" required className="form-control" value={formData.firstName} onChange={e => setFormData({...formData, firstName: e.target.value})} /></div>
                            <div className="form-group">
                                <label>Phone</label>
                                <input type="text" className="form-control" value={formData.phone} onChange={e => {
                                    setFormData({...formData, phone: e.target.value});
                                    if (e.target.value && !/^\d{10}$|^\d{3}-\d{3}-\d{4}$/.test(e.target.value)) setPhoneError('The Phone field is not a valid phone number.');
                                    else setPhoneError('');
                                }} />
                                {phoneError && <span className="text-danger">{phoneError}</span>}
                            </div>
                            <div className="form-group">
                                <label>Employee Type</label>
                                <select className="form-control" value={formData.employeeType} onChange={e => setFormData({...formData, employeeType: e.target.value})}>
                                    <option>Contractor</option><option>Full-Time</option><option>Part-Time</option>
                                </select>
                            </div>
                            <div className="form-group"><label>Company Name</label><input type="text" className="form-control" value={formData.companyName} onChange={e => setFormData({...formData, companyName: e.target.value})} /></div>
                            <div className="form-group"><label>User Is Active</label><input type="checkbox" checked={formData.active} onChange={e => setFormData({...formData, active: e.target.checked})} /></div>
                            
                            <div className="form-group" style={{marginLeft: '165px'}}>
                                <button type="submit" className="btn btn-primary">{view === 'edit' ? 'Save' : 'Create'}</button>
                            </div>
                        </form>
                        <a href="#" onClick={() => setView('list')} className="action-link">Back to List</a>
                    </div>
                )}

                {selectedUser && view === 'list' && (
                    <div className="modal-overlay">
                        <div className="modal-content">
                            <div className="modal-header"><h3>User Details</h3><button onClick={() => setSelectedUser(null)} style={{border:'none', background:'none', fontSize:'20px'}}>&times;</button></div>
                            <div className="modal-body">
                                <div className="detail-row"><div className="detail-label">Email Address</div><div>{selectedUser.email}</div></div>
                                <div className="detail-row"><div className="detail-label">Last Name</div><div>{selectedUser.lastName}</div></div>
                                <div className="detail-row"><div className="detail-label">First Name</div><div>{selectedUser.firstName}</div></div>
                                <div className="detail-row"><div className="detail-label">Phone Number</div><div>{selectedUser.phone}</div></div>
                                <div className="detail-row"><div className="detail-label">Employee Type</div><div>{selectedUser.employeeType}</div></div>
                                <div className="detail-row"><div className="detail-label">Company Name</div><div>{selectedUser.companyName}</div></div>
                                <div className="detail-row"><div className="detail-label">User Is Active</div><div>{selectedUser.active ? 'Yes' : 'No'}</div></div>
                            </div>
                            <div className="modal-footer"><button className="btn" onClick={() => setSelectedUser(null)}>Close</button></div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default App;
