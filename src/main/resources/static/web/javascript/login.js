const {createApp} = Vue

const login = createApp({
    data(){
        return {
            accountClient:[],
            clientsAccount: {},
            email: "",
            password: "",
            errorLogin : "",
            firstNameSign: "",
            lastNameSign: "",
            emailSign: "",
            passwordSign: "",
            errorRegister: "",
            
           


            
            
            
        }
    },
    created(){
        
        this.checkedAccount = localStorage.getItem("mode") 
    },
    methods: {
        login(){
            axios.post('/api/login', `email=${this.email}&password=${this.password}`)
                .then(response => {
                   window.location.href = '/web/accounts.html'
                    
                })
                .catch(error => {
                    this.errorLogin = "Email or password incorrect."
                });
        },
        logout() {
            axios.post('/api/logout').then(response => {
                
                window.location.href = './index.html'
            })
        },
        signup() {
            axios.post('/api/clients', `firstName=${this.firstNameSign}&lastName=${this.lastNameSign}&email=${this.emailSign}&password=${this.passwordSign}`)
                .then(response => {

                    this.email = this.emailSign
                    this.password = this.passwordSign
                    this.login()
                })
                .catch(error => {
                    if (error.response.data == 'Missing data') {
                        this.errorRegister = "Missing Data";
                    }
                    if (error.response.data == 'Email already in use') {
                        this.errorRegister = "Email already in use";
                    }
                })
        },
        
       
        
        
            
    },
    computed: {
      

    
    
    
    }
})


login.mount('#login')